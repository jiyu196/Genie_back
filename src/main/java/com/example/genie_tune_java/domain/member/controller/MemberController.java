package com.example.genie_tune_java.domain.member.controller;

import com.example.genie_tune_java.api.nts.service.BusinessNumberCheckService;
import com.example.genie_tune_java.domain.member.dto.*;
import com.example.genie_tune_java.domain.member.service.MemberService;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MemberController {
  private final MemberService memberService;
  private final BusinessNumberCheckService businessNumberCheckService;

  @MutationMapping
  public BusinessValidationCheckResponseDTO checkBizNumber(@Argument("input") BusinessValidationCheckRequestDTO dto) {
    return businessNumberCheckService.checkBusinessValidation(dto);
  }

  @MutationMapping
  public MemberRegisterResponseDTO register(@Argument("input") MemberRegisterRequestDTO dto) {
    return memberService.register(dto);
  }

  @QueryMapping
  @PreAuthorize("isAuthenticated()") // 인증된 사람이면 전부 내 정보 가져오기 가능
  public MemberGetResponseDTO me() {
    return memberService.getMember();
  }

}
