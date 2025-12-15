package com.example.genie_tune_java.security.controller;

import com.example.genie_tune_java.domain.member.dto.MemberLoginRequestDTO;
import com.example.genie_tune_java.domain.member.dto.MemberLoginResponseDTO;
import com.example.genie_tune_java.security.service.AuthService;
import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Log4j2
public class AuthController {
  private final AuthService authService;

  @MutationMapping
  public MemberLoginResponseDTO login(@Argument("input") MemberLoginRequestDTO dto, DataFetchingEnvironment env) throws Exception {
    return authService.memberLogin(dto, env);
  }
}
