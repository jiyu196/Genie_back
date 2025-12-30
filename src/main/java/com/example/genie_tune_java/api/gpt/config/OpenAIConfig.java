package com.example.genie_tune_java.api.gpt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAIConfig {
  @Value("${OPENAI_API_KEY}")
  private String openAIkey;

  @Value("${OPENAI_URL}")
  private String openAIUrl;

  @Bean
  public WebClient openAIWebClient(WebClient.Builder builder) {
    return builder
            .baseUrl(openAIUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION,"Bearer " + openAIkey)
            .build();
  }
}
