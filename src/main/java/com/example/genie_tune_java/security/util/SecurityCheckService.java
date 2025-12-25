package com.example.genie_tune_java.security.util;

import com.example.genie_tune_java.security.dto.JWTPrincipal;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component("auth")
public class SecurityCheckService {

  // 현재 활동 가능한 회원 여부
  public boolean isActive(JWTPrincipal principal) {
    return "ACTIVE".equals(principal.getAccountStatus());
  }

  // 가입 승인 여부
  public boolean isApproved(JWTPrincipal principal) {
    return "APPROVED".equals(principal.getRegisterStatus());
  }

  // 역할 체크
  public boolean hasAnyRole(JWTPrincipal principal, String... roles) {
    if(principal == null || principal.getRole() == null ) return false;

    return Arrays.asList(roles).contains(principal.getRole());
  }

  // 3. 복합 체크 (조합)
  public boolean isNormalUser(JWTPrincipal principal) {
    // 기본 조건 충족 + (MEMBER 또는 SUBSCRIBER 또는 ADMIN 권한 보유)
    return isActive(principal) && isApproved(principal) &&
            hasAnyRole(principal, "MEMBER", "SUBSCRIBER", "ADMIN");
  }
}
