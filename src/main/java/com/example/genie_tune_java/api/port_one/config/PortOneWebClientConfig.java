package com.example.genie_tune_java.api.port_one.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class PortOneWebClientConfig {

    @Value("${PORTONE_BASE_URL}")
    private String baseUrl;

    @Bean
    public WebClient portOneWebClient(WebClient.Builder webClientBuilder) {
      return webClientBuilder
              .baseUrl(baseUrl)
              .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              // 필요하다면 여기서 타임아웃이나 로깅 필터를 추가 설정
              .build();
    }
}
