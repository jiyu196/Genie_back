package com.example.genie_tune_java.api.nts;

import com.example.genie_tune_java.api.nts.dto.BusinessStatusRequestDTO;
import com.example.genie_tune_java.api.nts.dto.BusinessStatusResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;


@Component
@RequiredArgsConstructor
public class NTSBusinessAPIClient {
  private final WebClient webClient;

  @Value("${nts.api.key}")
  private String serviceKey;

  //String url = "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey?=" + serviceKey;

  /**
   * 사업자 상태 조회 (계속사업자 / 폐업자 / 휴업자)
   */
  public BusinessStatusResponseDTO checkStatus(String bizNumber) {

    return webClient.post()
            .uri(URI.create("https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=" + serviceKey))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(new BusinessStatusRequestDTO(bizNumber))
            .retrieve()
            .bodyToMono(BusinessStatusResponseDTO.class)
            .block();
  }
}
