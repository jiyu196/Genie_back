package com.example.genie_tune_java.api.nts;

import com.example.genie_tune_java.api.nts.dto.validation.BusinessValidationRequestDTO;
import com.example.genie_tune_java.api.nts.dto.validation.BusinessValidationResponseDTO;
import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.member.dto.register.biz_check.BusinessValidationCheckRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;


@Component
public class NTSBusinessAPIClient {
  private final WebClient webClient;

  @Value("${nts.api.key}")
  private String serviceKey;

  public NTSBusinessAPIClient(WebClient.Builder builder) {
    this.webClient = builder.clone() // 복제해서 사용 (설정 전파 방지)
            .baseUrl("https://api.odcloud.kr")
            .build();
  }

  public BusinessValidationResponseDTO checkValidation(BusinessValidationCheckRequestDTO dto) {

    return webClient.post()
            .uri(uriBuilder -> uriBuilder
                    .path("/api/nts-businessman/v1/validate")
                    .queryParam("serviceKey", serviceKey)
                    .build())
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
