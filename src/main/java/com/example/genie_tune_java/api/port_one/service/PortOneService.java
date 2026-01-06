package com.example.genie_tune_java.api.port_one.service;

import com.example.genie_tune_java.api.port_one.dto.PortOneGetPaymentsRequestDTO;
import com.example.genie_tune_java.api.port_one.dto.PortOneGetPaymentsResponseDTO;
import com.example.genie_tune_java.api.port_one.dto.PortOnePreRegisterRequestDTO;
import com.example.genie_tune_java.api.port_one.dto.PortOnePreRegisterResponseDTO;
import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
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

  public PortOneGetPaymentsResponseDTO getPayments(PortOneGetPaymentsRequestDTO dto) {
    String accessToken = portOneAuthService.getAccessToken();
    log.info("결제 성공 후, 결제 정보 가져오기");
    try {
      PortOneGetPaymentsResponseDTO responseDTO = portOneWebClient.get()
              .uri("/payments/{paymentId}", dto.paymentId())
              .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
              .retrieve()
              .onStatus(HttpStatusCode::isError, response -> {
                return switch (response.statusCode().value()) {
                  case 400 -> Mono.error(new GlobalException(ErrorCode.PAYMENT_GET_INVALID_REQUEST_ERROR));
                  case 401 -> Mono.error(new GlobalException(ErrorCode.PAYMENT_GET_UNAUTHORIZED_ERROR));
                  case 403 -> Mono.error(new GlobalException(ErrorCode.PAYMENT_GET_FORBIDDEN_ERROR));
                  case 404 -> Mono.error(new GlobalException(ErrorCode.PAYMENT_GET_PAYMENT_NOT_FOUND_ERROR));
                  default -> Mono.error(new GlobalException(ErrorCode.INTERNAL_ERROR));
                };
              })
              .bodyToMono(PortOneGetPaymentsResponseDTO.class)
              .doOnNext(log::info)
              .block();
      log.info("결제 정보 확인 완료 : {}", responseDTO);
      return responseDTO;
    } catch (Exception e) {
      log.error("결제정보 조회 실패 {}", e.getMessage());
      throw new GlobalException(ErrorCode.INTERNAL_ERROR);
    }
  }
}
