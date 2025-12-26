package com.example.genie_tune_java.security.service;
import com.example.genie_tune_java.domain.member.entity.Member;

import com.example.genie_tune_java.security.util.CookieUtil;
import com.example.genie_tune_java.security.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Log4j2
@Service
public class JWTService {
  private final JWTUtil jwtUtil;
  private final CookieUtil cookieUtil;

  public ResponseCookie generateAccessTokenWithCookie(Member loginMember) {
    String accessToken = jwtUtil.createAccessToken(loginMember.getId(), loginMember.getRole().toString(), loginMember.getAccountStatus(), loginMember.getRegisterStatus());
    return cookieUtil.createCookie(accessToken, "Access_Cookie");
  }
  public ResponseCookie generateRefreshTokenWithCookie(String randomUuid) {
    String refreshToken = jwtUtil.createRefreshToken(randomUuid);
    return cookieUtil.createCookie(refreshToken, "Refresh_Cookie");
  }

}
