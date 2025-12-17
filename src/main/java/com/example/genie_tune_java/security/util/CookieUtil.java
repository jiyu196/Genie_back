package com.example.genie_tune_java.security.util;

import com.example.genie_tune_java.common.exception.GlobalException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class CookieUtil {

  private final JWTUtil jwtUtil;

  public ResponseCookie createAccessCookie(String accessToken) {
    //1. accessToken에 저장된 만료시간
    Date tokenExpiration = jwtUtil.getExpiration(accessToken);
    // 2. JWTToken 기준 만료시간에서 현재 시스템에 저장된 시간을 빼서 남은 시간 계산 .getTime()도 1970년 1월 1일인가? 기준의 밀리세컨즈로 반환
    long LeftTime = (tokenExpiration.getTime() - System.currentTimeMillis())/ 1000;
    log.info("토큰 남은 시간: {} 남은 시간(초단위): {}", tokenExpiration, LeftTime);
    // key, value 형태로 cookie에 accessToken을 value(String)로 저장한다.
    ResponseCookie accessCookie = ResponseCookie.from("Access_Cookie", accessToken)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .sameSite("Lax")
            // maxAge는 쿠키의 만료 시간을 초로 지정한다. 그래서 위의 Mills의 계산 값을 1000으로 나눈것
            .maxAge(LeftTime)
            .build();
    log.info("만들어진 Cookie {}", accessCookie);
    return accessCookie;
  }
  // Cookie를 삭제하는 로직은 동일한 이름의 쿠키에 maxAge를 0으로 만들어버리면 삭제가 된다.
  public ResponseCookie deleteAccessCookie() {
    return ResponseCookie.from("Access_Cookie", "")
            .httpOnly(true)
            .secure(false)
            .path("/")
            .sameSite("Lax")
            .maxAge(0)
            .build();
  }

  public String getCookieValue(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    //Http Header에 저장해놓은 Cookie 배열이 존재하지 않으면 null을 반환하므로 null check 필요
    if(cookies != null) {
      for (Cookie ck : cookies) {
        if(ck.getName().equals("Access_Cookie")) {
          return ck.getValue();
        }
      }
    }
    //일단 Cookie 내에 Access_Cookie 없으면 null 반환
    return null;
  }


}
