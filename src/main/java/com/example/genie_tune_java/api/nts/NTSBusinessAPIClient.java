package com.example.genie_tune_java.api.nts;

import com.example.genie_tune_java.api.nts.dto.status.BusinessStatusRequestDTO;
import com.example.genie_tune_java.api.nts.dto.status.BusinessStatusResponseDTO;
import com.example.genie_tune_java.api.nts.dto.validation.BusinessValidationOutputData;
import com.example.genie_tune_java.api.nts.dto.validation.BusinessValidationRequestDTO;
import com.example.genie_tune_java.api.nts.dto.validation.BusinessValidationResponseDTO;
import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.member.dto.BusinessValidationCheckRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
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

  public BusinessValidationResponseDTO checkValidation(BusinessValidationCheckRequestDTO dto) {

    return webClient.post()
            .uri(URI.create("https://api.odcloud.kr/api/nts-businessman/v1/validate?serviceKey=" + serviceKey))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(new BusinessValidationRequestDTO(dto))
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                    .map(body -> new GlobalException(
                            ErrorCode.HTTP_ERROR
                    ))
            )
            .bodyToMono(BusinessValidationResponseDTO.class)
            .block();
  }
}
