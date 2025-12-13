package com.example.genie_tune_java.common.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter // 얘 없으면 yml 파일 못 가져옴
@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTProperties {
  private String secret;
  private Long accessTokenExpiration;
  private Long refreshTokenExpiration;
}
