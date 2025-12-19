package com.example.genie_tune_java.security.util;

import com.example.genie_tune_java.security.status.TokenStatus;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Log4j2
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
  //AccessToken 만드는 로직
  public String createAccessToken(Long memberId, String memberRole) {
    return generateJWTToken(memberId.toString(), "access", memberRole);
  }
  //RefreshToken 만드는 로직
  public String createRefreshToken(String refreshTokenUuid, String memberRole) {
    return generateJWTToken(refreshTokenUuid, "refresh", memberRole);
  }

  //Token 을 만드는 메서드 access 인지 refresh인지 입력하여 나눠서 발급한다.
  private String generateJWTToken(String subValue, String TokenCategory, String memberRole) {
    //access는 memberId, Role을 넣고
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + (TokenCategory.equals("access") ? accessTokenExpiration : refreshTokenExpiration));
     JwtBuilder builder = Jwts.builder()
            .subject(subValue)
            .claim("TokenCategory", TokenCategory)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key);

     if (TokenCategory.equals("access")) {
       builder.claim("MemberRole", memberRole);
     }

    return builder.compact();
  }


  // 토큰 파싱 및 검증까지 포함된 로직 (성공하면 Claim 객체 반환)
  private Claims getClaims(String token) {
      return Jwts.parser()
              .verifyWith(key)
              .build()
              .parseSignedClaims(token)
              .getPayload();
  }
  // 파싱 및 검증 성공 이후에 JWTToken Subject에 심어놓은 pk 값 추출
  public Long getMemberId(String token) {
    return Long.parseLong(getClaims(token).getSubject());
  }
  public String getUuid(String token) {return getClaims(token).getSubject();}
  public String getMemberRole(String token) {
    return getClaims(token).get("MemberRole", String.class);
  }

  // 파싱 및 검증을 통해서 토큰의 현재 상태를 확인
  public TokenStatus checkToken(String token) {
    try{
      getClaims(token);
      return TokenStatus.VALID;
    } catch(ExpiredJwtException e) {
      log.warn("토큰만료 : {}", e.getMessage());
      return TokenStatus.EXPIRED;
    } catch(SecurityException e) {
      log.warn("서명 불일치 : {}", e.getMessage());
      return TokenStatus.INVALID;
    } catch (MalformedJwtException e) {
      log.warn("토큰 형식 이상 {}", e.getMessage());
      return TokenStatus.MALFORMED;
    } catch (Exception e) {
      log.warn("지원하지 않는 토큰 형식 또는 알 수 없는 오류 : {}", e.getMessage());
      return TokenStatus.UNSUPPORTED;
    }
  }

  public Date getExpiration(String token) {
    return getClaims(token).getExpiration();
  }

}
