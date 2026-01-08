package com.example.genie_tune_java.api.gpt.service;

import com.example.genie_tune_java.api.gpt.dto.OpenAIRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class OpenAIService {
  private final WebClient pythonWebClient;

  public OpenAIService(WebClient.Builder builder, @Value("http://localhost:8000") String pythonUrl) {

    this.pythonWebClient = builder.clone()
            .baseUrl(pythonUrl)
            .build();
  }

  // 1. 순수하게 파이썬 서버에 데이터를 던지고 결과를 받는 기능
  public Mono<String> requestPythonProcessing(OpenAIRequestDTO dto) {
    return pythonWebClient.post()
            .uri("/image/generate")
            .bodyValue(dto)
            .retrieve()
            .bodyToMono(String.class);
  }

}
