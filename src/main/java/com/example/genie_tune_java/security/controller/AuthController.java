package com.example.genie_tune_java.security.controller;

import com.example.genie_tune_java.domain.member.dto.login.MemberLoginRequestDTO;
import com.example.genie_tune_java.domain.member.dto.login.MemberLoginResponseDTO;
import com.example.genie_tune_java.domain.service_access.dto.login.ServiceAccessLoginRequestDTO;
import com.example.genie_tune_java.domain.service_access.dto.login.ServiceAccessLoginResponseDTO;
import com.example.genie_tune_java.security.service.AuthService;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Log4j2
public class AuthController {
  private final AuthService authService;

  @MutationMapping
  public MemberLoginResponseDTO login(@Argument("input") MemberLoginRequestDTO dto, DataFetchingEnvironment env) throws Exception {
    log.info(authService.memberLogin(dto, env));
    return authService.memberLogin(dto, env);
  }

  @MutationMapping
  @PreAuthorize("isAuthenticated()")
  public Boolean logout(DataFetchingEnvironment env) throws Exception {
    log.info(env);
    return authService.memberLogout(env);
  }

  @MutationMapping
  public ServiceAccessLoginResponseDTO serviceAccessLogin(ServiceAccessLoginRequestDTO dto, DataFetchingEnvironment env) {
    return authService.serviceAccessLogin(dto, env);
  }

}
