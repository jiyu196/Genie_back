package com.example.genie_tune_java.api.gpt.service;

import com.example.genie_tune_java.api.gpt.dto.DownloadImageDTO;
import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.net.URI;


@Service
@RequiredArgsConstructor
@Log4j2
public class DownloadImageService {

  private final WebClient.Builder webClientBuilder;

  public DownloadImageDTO download(String imageUrl) {
    // 1. 리다이렉트(302)를 자동으로 따라가도록 HttpClient 설정
    HttpClient httpClient = HttpClient.create().followRedirect(true);

    // 2. Builder를 활용하여 설정을 추가한 WebClient 빌드
    WebClient client = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10MB
            .build();


    try {
      return client.get()
              .uri(URI.create(imageUrl))
              // Azure Blob Storage는 아래 헤더들이 없으면 403을 뱉을 때가 많습니다.
              .header(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
              .header(HttpHeaders.ACCEPT, "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8")
              .header("sec-ch-ua", "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"")
              .header("sec-ch-ua-mobile", "?0")
              .header("sec-ch-ua-platform", "\"Windows\"")
              .retrieve()
              .onStatus(HttpStatusCode::isError, response -> {
                log.error("응답 에러 코드: {}", response.statusCode());
                return Mono.error(new GlobalException(ErrorCode.IMAGE_GET_FAILED));
              })
              .toEntity(byte[].class)
              .map(response -> {
                byte[] bytes = response.getBody();
                if (bytes == null || bytes.length < 1000) {
                  throw new GlobalException(ErrorCode.IMAGE_GET_FAILED);
                }

                String contentType = response.getHeaders().getContentType() != null
                        ? response.getHeaders().getContentType().toString()
                        : "image/png";

                return new DownloadImageDTO(bytes, contentType, bytes.length);
              })
              .block();
    } catch (Exception e) {
      log.error("이미지 다운로드 예외 발생: {}", e.getMessage());
      throw new GlobalException(ErrorCode.IMAGE_GET_FAILED);
    }
  }

}
