package com.example.genie_tune_java.api.nts;

import com.example.genie_tune_java.api.nts.dto.BusinessStatusRequestDTO;
import com.example.genie_tune_java.api.nts.dto.BusinessStatusResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
@RequiredArgsConstructor
public class NTSBusinessAPIClient {
  private final WebClient webClient;

  @Value("${nts.api.key}")
  private String serviceKey;

  private static final String HOST = "api.odcloud.kr";
  private static final String STATUS_PATH = "/api/nts-businessman/v1/status";

  /**
   * 사업자 상태 조회 (계속사업자 / 폐업자 / 휴업자)
   */
  public BusinessStatusResponseDTO checkStatus(String bizNumber) {

    return webClient.post()
            .uri(uriBuilder -> uriBuilder
                    .scheme("https")
                    .host(HOST)
                    .path(STATUS_PATH)
                    .queryParam("serviceKey", serviceKey)
                    .build()
            )
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new BusinessStatusRequestDTO(bizNumber))
            .retrieve()
            .bodyToMono(BusinessStatusResponseDTO.class)
            .block();
  }
}
