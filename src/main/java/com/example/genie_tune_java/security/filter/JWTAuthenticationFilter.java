package com.example.genie_tune_java.security.filter;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.common.util.RedisUtil;
import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.member.repository.MemberRepository;
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

import org.springframework.http.ResponseCookie;
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
  private final RedisUtil redisUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    // 1. Cookie에서 JWT TOKEN 추출

    String accessToken = cookieUtil.getCookieValue(request, "Access_Cookie");
    String refreshToken = cookieUtil.getCookieValue(request, "Refresh_Cookie");

    log.info("Access Token: {} Refresh Token: {}", accessToken, refreshToken);
    // 2. Token이 있을 경우, 사용자 정보 추출 (RefreshToken 관련 추후 추가)
    if (accessToken != null){
      log.info("accessToken이 있는 경우 내부 로직");
      // 2-1. Token 유효성 검사 Method JWTUtil에 존재
      if(jwtUtil.checkToken(accessToken) != TokenStatus.VALID) {
        log.info("Token이 유효하지 않은 경우");
        // 다음 필터로 보내버리고 로직 종료
        filterChain.doFilter(request, response);
        return;
      }

      log.info("AccessToken이 정상인 경우 RefreshToken은 check하지 않고 인증객체를 등록합니다.");

      // 2-2. accessToken에 저장되어 있는 pk랑 role 꺼내기
      Long memberId = jwtUtil.getMemberId(accessToken);
      String role = jwtUtil.getMemberRole(accessToken);
      log.info("accessToken에서 값 추출 {} {}", memberId, role);

      // 2-3. 추출한 정보를 Principal 객체에 담는다.
      JWTPrincipal principal = new JWTPrincipal(memberId, role);

      log.info("principal 생성 {}", principal);
    // 2-4. 그 이후 Authentication 객체 생성 credentials는 JWT Parser에서 검증했으므로 null을 집어넣는다. principal과 authority를 담는다.

      Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

      log.info("authentication 객체 생성");

    //2-5. 만들어진 Authentication 객체 Security Context에 설정
      SecurityContextHolder.getContext().setAuthentication(authentication);

    // 3. AccessToken이 없을 때는 아래 로직
    } else {
      log.info("accessCookie 존재 x");
      //3-1. RefreshToken 유효성 검증
      if(jwtUtil.checkToken(refreshToken) == TokenStatus.VALID) {
        log.info("refreshCookie 정상");
        //3-2. Redis에 저장된 value값을 꺼내서 조치 ex) 7:MEMBER -> [7, MEMBER] 형태로 변환
        String[] redisValue = (redisUtil.get("RT:" + jwtUtil.getUuid(refreshToken))).split(":");
        Long memberId = Long.parseLong(redisValue[0]);
        String role = redisValue[1];
        log.info("memberId: {} role: {}",memberId, role);

        // 3-3. redis 정보 토대로 accessToken 생성
        String newAccessToken = jwtUtil.createAccessToken(memberId, role);
        // 3-4. 새로운 accessToken 기반 -> AccessCookie 생성
        ResponseCookie accessCookie = cookieUtil.createCookie(newAccessToken, "ACCESS_COOKIE");

        // 3-5. 쿠키 브라우저 발급 로직 필요 아예 이 로직 자체를 authService로 빼야하나 고민 중
        response.addHeader("Set-Cookie", accessCookie.toString());

        // 3-6. 인증 객체 생성
        JWTPrincipal principal = new JWTPrincipal(memberId, role);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        // 3-7. Security Context에 등록
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }
    //6. Anonymous User 대상 다음 필터로 진행 Controller 단에서 PreAuthorize 어노테이션 등에 대해서 걸러질 예정
    filterChain.doFilter(request, response);
    log.info("JWT Token Authentication Filter 인증 끝");
  }
}
