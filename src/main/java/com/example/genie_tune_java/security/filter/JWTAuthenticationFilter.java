package com.example.genie_tune_java.security.filter;

import com.example.genie_tune_java.security.dto.JWTPrincipal;
import com.example.genie_tune_java.security.util.CookieUtil;
import com.example.genie_tune_java.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Principal;

@Log4j2
@RequiredArgsConstructor
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

  private final CookieUtil cookieUtil;
  private final JWTUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    // 1. Cookie에서 JWT TOKEN 추출

    String accessToken = cookieUtil.getCookieValue(request);

    // 2. Token이 있을 경우, 사용자 정보 추출 (RefreshToken 관련 추후 추가)
    if (accessToken != null){
      Long memberId = jwtUtil.getMemberId(accessToken);
      String role = jwtUtil.getMemberRole(accessToken);
    // 3. 추출한 정보를 Principal 객체에 담는다.
      JWTPrincipal principal = new JWTPrincipal(memberId, role);
    // 4. 그 이후 Authentication 객체 생성 credentials는 JWT Parser에서 검증했으므로 null을 집어넣는다. principal과 authority를 담는다.
      Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

    //5. 만들어진 Authentication 객체 Security Context에 설정
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    //6. Anonymous User 대상 다음 필터로 진행
    filterChain.doFilter(request, response);
  }
}
