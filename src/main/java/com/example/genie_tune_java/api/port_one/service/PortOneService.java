package com.example.genie_tune_java.api.port_one.service;

import com.example.genie_tune_java.api.port_one.dto.PortOnePreRegisterRequestDTO;
import com.example.genie_tune_java.api.port_one.dto.PortOnePreRegisterResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Log4j2
@RequiredArgsConstructor
public class PortOneService {
  private final WebClient portOneWebClient;
  private final PortOneAuthService portOneAuthService;

  public PortOnePreRegisterResponseDTO preRegister(String orderUuid , PortOnePreRegisterRequestDTO dto) {
    String accessToken = portOneAuthService.getAccessToken();

    try{
      portOneWebClient.post()
              .uri("/payments/{paymentId}/pre-register", orderUuid)
              .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
              .bodyValue(dto)
              .retrieve()
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
