package com.example.genie_tune_java.api.port_one.service;

import com.example.genie_tune_java.api.port_one.dto.PortOnePreRegisterRequestDTO;
import com.example.genie_tune_java.api.port_one.dto.PortOnePreRegisterResponseDTO;
import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class PortOneService {
  private final WebClient portOneWebClient;
  private final PortOneAuthService portOneAuthService;

  public PortOnePreRegisterResponseDTO preRegister(String orderUuid , PortOnePreRegisterRequestDTO dto) {
    String accessToken = portOneAuthService.getAccessToken();
    log.info("여기는 PortOnePreRegister 로직");
    try{
      portOneWebClient.post()
              .uri("/payments/{paymentId}/pre-register", orderUuid)
              .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
              .bodyValue(dto)
              .retrieve()
              .onStatus(HttpStatusCode::isError, response -> {
                return switch (response.statusCode().value()) {
                  case 400 -> Mono.error(new GlobalException(ErrorCode.PAYMENT_PRE_REGISTER_INVALID_REQUEST_ERROR));
                  case 401 -> Mono.error(new GlobalException(ErrorCode.PAYMENT_PRE_REGISTER_UNAUTHORIZED));
                  case 403 -> Mono.error(new GlobalException(ErrorCode.PAYMENT_PRE_REGISTER_FORBIDDEN_ERROR));
                  case 409 -> Mono.error(new GlobalException(ErrorCode.PAYMENT_PRE_REGISTER_ALREADY_PAID_ERROR));
                  default ->  Mono.error(new GlobalException(ErrorCode.INTERNAL_ERROR));
                };
              })
              .toBodilessEntity()
              .block();
      log.info("주문에 대한 결제 사전 등록 완료! {}", orderUuid);
      // true를 보고 프론트에서 결제창을 띄움
      return new PortOnePreRegisterResponseDTO(true);

    } catch (Exception e){
      log.error("결제 사전등록 실패 {}", e.getMessage());
    }

    return new PortOnePreRegisterResponseDTO(false);
  }


}
