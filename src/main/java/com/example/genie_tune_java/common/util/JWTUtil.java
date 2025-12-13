package com.example.genie_tune_java.common.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
  private final SecretKey key;
  private final Long accessTokenExpiration;
  private final Long refreshTokenExpiration;
  public JWTUtil(JWTProperties jwtProperties) {
    //스트링으로 받아온 key를 SecretKey type으로 바꿔오는 작업
    this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    this.accessTokenExpiration = jwtProperties.getAccessTokenExpiration();
    this.refreshTokenExpiration = jwtProperties.getRefreshTokenExpiration();
  }

  public String createAccessToken(Long userId) {
    return generateJWTToken(userId, "access");
  }


  //Token 을 만드는 메서드 access 인지 refresh인지 입력하여 나눠서 발급한다.
  private String generateJWTToken(Long userId, String TokenCategory) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + (TokenCategory.equals("access") ? accessTokenExpiration : refreshTokenExpiration));
    return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact();
  }


}
