package com.example.genie_tune_java.api.gpt.service;

import com.example.genie_tune_java.api.gpt.dto.OpenAIRequestDTO;
import com.example.genie_tune_java.api.gpt.dto.OpenAIResponseDTO;
import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@Log4j2
public class OpenAIService {
  private final WebClient pythonWebClient;

  public OpenAIService(WebClient.Builder builder, @Value("${PYTHON_ADDRESS}") String pythonUrl) {

    this.pythonWebClient = builder.clone()
            .baseUrl(pythonUrl)
            .build();
  }

  // 1. 순수하게 파이썬 서버에 데이터를 던지고 결과를 받는 기능
  public OpenAIResponseDTO requestPythonProcessing(OpenAIRequestDTO dto) {
    return pythonWebClient.post()
            .uri("api/v1/image/generate")
            .bodyValue(dto)
            .retrieve()
            .onStatus(
                    status -> status.is4xxClientError() || status.is5xxServerError(),
                    response -> response.bodyToMono(String.class)
                            .map(body -> new GlobalException(ErrorCode.OPENAI_PYTHON_SERVER_ERROR))
            )
            .bodyToMono(OpenAIResponseDTO.class)
            .block();
  }

}
