package com.example.genie_tune_java.security.service;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.common.util.RedisUtil;
import com.example.genie_tune_java.domain.member.dto.MemberLoginRequestDTO;
import com.example.genie_tune_java.domain.member.dto.MemberLoginResponseDTO;
import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.member.repository.MemberRepository;
import com.example.genie_tune_java.security.util.CookieUtil;
import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {
  private final MemberRepository memberRepository;
  private final JWTService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final CookieUtil cookieUtil;
  private final RedisUtil redisUtil;


  public MemberLoginResponseDTO memberLogin(MemberLoginRequestDTO dto, DataFetchingEnvironment env) throws Exception {

    // 1. 이메일 존재여부 확인
    Member loginMember = memberRepository.findByEmail(dto.email()).orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

    // 2. Member Entity 꺼내서 비밀번호 검증

    if(!passwordEncoder.matches(dto.password(), loginMember.getPassword())){
      throw new GlobalException(ErrorCode.MEMBER_PASSWORD_INVALID);
    }

    // 3. ResponseCookie에 JWTToken (AccessToken, RefreshToken) 각각 담아서 설정

    ResponseCookie accessCookie = jwtService.generateAccessTokenWithCookie(loginMember);
    String randomUuid = UUID.randomUUID().toString();
    ResponseCookie refreshCookie = jwtService.generateRefreshTokenWithCookie(randomUuid);
    //4. RefreshToken 설정 값을 Redis에 넣기
    long ttlSeconds = refreshCookie.getMaxAge().getSeconds();
    //인증 객체 생성시 필요한 pk와 role 값을 redis에서 꺼내기
    String redisValue = loginMember.getId().toString() + ":" + loginMember.getRole().toString();

    redisUtil.set("RT:" + randomUuid, redisValue, ttlSeconds * 1000); // 여기 TTL을 쿠키 시간과 동일하게

    HttpServletResponse response = env.getGraphQlContext().get(HttpServletResponse.class);

    response.addHeader("Set-Cookie", accessCookie.toString());
    response.addHeader("Set-Cookie", refreshCookie.toString());
    // 4. 성공 가정 true 반환

    return new MemberLoginResponseDTO(true);
  }

  public Boolean memberLogout(DataFetchingEnvironment env) throws Exception {
    log.info("logout 요청 진입");
    ResponseCookie deleteAccessCookie = cookieUtil.deleteCookie("Access_Cookie");
    ResponseCookie deleteRefreshCookie = cookieUtil.deleteCookie("Refresh_Cookie");
    log.info("AccessCookie: {} RefreshCookie: {}",deleteAccessCookie, deleteRefreshCookie);
    HttpServletResponse response = env.getGraphQlContext().get(HttpServletResponse.class);
    response.addHeader("Set-Cookie", deleteAccessCookie.toString());
    response.addHeader("Set-Cookie", deleteRefreshCookie.toString());
    return true;
  }
}
