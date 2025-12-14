package com.example.genie_tune_java.domain.member.controller;

import com.example.genie_tune_java.domain.member.dto.MemberRegisterRequestDTO;
import com.example.genie_tune_java.domain.member.dto.MemberRegisterResponseDTO;
import com.example.genie_tune_java.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MemberController {
  private final MemberService memberService;

  @MutationMapping
  public MemberRegisterResponseDTO register(@Argument("input") MemberRegisterRequestDTO dto) {
    return memberService.register(dto);
  }

}
