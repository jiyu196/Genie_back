package com.example.genie_tune_java.security.filter;

import com.example.genie_tune_java.security.dto.JWTPrincipal;
import com.example.genie_tune_java.security.status.TokenStatus;
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
      log.info("accessToken이 있는 경우 내부 로직");
      // Token 유효성 검사 Method JWTUtil에 존재
      if(jwtUtil.checkToken(accessToken) == TokenStatus.EXPIRED){
        log.info("만료시 로직");
        //토큰 재발급 로직 구현 (Redis 내의 RefreshToken과 일치하는지 여부 등등)
        // accessToken을 재할당하여 아래 if문을 통과하게 만들어주는 것도 중요
      }

      if(jwtUtil.checkToken(accessToken) != TokenStatus.VALID) {
        log.info("Token이 유효하지 않은 경우");
        // 다음 필터로 보내버리고 로직 종료
        filterChain.doFilter(request, response);
        return;
      }

      log.info("Token이 정상인 경우(재발급 포함)");
      Long memberId = jwtUtil.getMemberId(accessToken);
      String role = jwtUtil.getMemberRole(accessToken);
      log.info("accessToken에서 값 추출 {} {}", memberId, role);
    // 3. 추출한 정보를 Principal 객체에 담는다.
      JWTPrincipal principal = new JWTPrincipal(memberId, role);
      log.info("principal 생성 {}", principal);
    // 4. 그 이후 Authentication 객체 생성 credentials는 JWT Parser에서 검증했으므로 null을 집어넣는다. principal과 authority를 담는다.
      Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
      log.info("authentication 객체 생성");
    //5. 만들어진 Authentication 객체 Security Context에 설정
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    //6. Anonymous User 대상 다음 필터로 진행 Controller 단에서 PreAuthorize 어노테이션 등에 대해서 걸러질 예정
    filterChain.doFilter(request, response);
    log.info("JWT Token Authentication Filter 인증 끝");
  }

}
