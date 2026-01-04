package com.example.genie_tune_java.api.port_one.service;

import com.example.genie_tune_java.api.port_one.dto.PortOneAccessTokenResponseDTO;
import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class PortOneAuthService {
  private final WebClient portOneWebClient;
  private final RedisUtil redisUtil;

  @Value("${portone.api-secret}")
  private String apiSecret;

  @Value("${portone.base-url}")
  private String baseUrl;

  private static final String AT_KEY = "portone:access_token";
  private static final String RT_KEY = "portone:refresh_token";

  public String getAccessToken() {
    // 1. Redis에서 Access Token 확인
    String at = redisUtil.get(AT_KEY);
    log.info("Access Token 존재 하는가?: {}" , at);
    if (at != null) return at;

    // 2. Access가 없으면 Refresh Token이 있는지 확인
    String rt = redisUtil.get(RT_KEY);
    log.info("Refresh Token: {}" , rt);
    if (rt != null) {
      try {
        // [추가될 로직] 포트원의 토큰 재발급 API 호출 (refresh-token API)
        PortOneAccessTokenResponseDTO dto = portOneWebClient.post()
                .uri("/token/refresh")
                .bodyValue(Map.of("refreshToken", rt))
                .retrieve()
                .bodyToMono(PortOneAccessTokenResponseDTO.class)
                .block();
        // token 유효성 검사하고 Redis에 저장하는 공용 메서드 호출
        return validateAndSaveTokens(dto);

      } catch (Exception e) {
        log.warn("Refresh Token 만료 또는 갱신 실패, 재로그인 시도");
      }
    }

    // 3. 둘 다 없다면 새로 로그인
    return authPortOne();
  }

  private String authPortOne() { //PortOne Token 새로 발급하는 로직

    try{
      PortOneAccessTokenResponseDTO dto = portOneWebClient.post()
              .uri("/login/api-secret")
              .bodyValue(Map.of("apiSecret", apiSecret))
              .retrieve()
              .bodyToMono(PortOneAccessTokenResponseDTO.class)
              .block();

      // token 유효성 검사하고 Redis에 저장하는 공용 메서드 호출
      return validateAndSaveTokens(dto);

    } catch (Exception e){
      log.error("PortOne 인증 실패: {}", e.getMessage());
      throw new GlobalException(ErrorCode.PORTONE_AUTH_FAILED);
    }
  }

  private String validateAndSaveTokens(PortOneAccessTokenResponseDTO dto) {
    // dto가 null이거나 accessToken 또는 refreshToken이 null이면 예외 발생 (PortOne 인증 실패)
    if (dto == null || dto.accessToken() == null || dto.refreshToken() == null) {
      throw new GlobalException(ErrorCode.PORTONE_AUTH_FAILED);
    }

    redisUtil.set(AT_KEY, dto.accessToken(), 1500L * 1000);
    redisUtil.set(RT_KEY, dto.refreshToken(), 86400L * 1000); // portone은 토큰 재발급을 해도 refreshToken도 재발급 함

    return dto.accessToken();
  }

}
