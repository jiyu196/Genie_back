package com.example.genie_tune_java.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    // 1. Cookie에서 JWT TOKEN 추출

    // 2. Token이 있을 경우, 사용자 정보 추출

    // 3. 그 이후 Authentication 객체 생성

    // 4. Authentication 객체를 SecurityContext에 등록 (다음 API 요청 이전까지 유효한 Context)


  }
}
