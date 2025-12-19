package com.example.genie_tune_java.domain.member.controller;

import com.example.genie_tune_java.api.nts.service.BusinessNumberCheckService;
import com.example.genie_tune_java.domain.member.dto.*;
import com.example.genie_tune_java.domain.member.dto.register.MemberRegisterRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.MemberRegisterResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.biz_check.BusinessValidationCheckRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.biz_check.BusinessValidationCheckResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.send_code.MemberVerifyEmailRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.send_code.MemberVerifyEmailResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.verify_code.MemberVerifyCodeRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.verify_code.MemberVerifyCodeResponseDTO;
import com.example.genie_tune_java.domain.member.service.MemberService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
public class MemberController {
  private final MemberService memberService;
  private final BusinessNumberCheckService businessNumberCheckService;

  @MutationMapping
  public MemberVerifyEmailResponseDTO sendVerificationEmail(@Argument("input") MemberVerifyEmailRequestDTO dto) throws MessagingException, UnsupportedEncodingException {
    return memberService.sendVerificationCode(dto);
  }

  @MutationMapping
  public MemberVerifyCodeResponseDTO verifyCode(@Argument("input")MemberVerifyCodeRequestDTO dto) {
    return memberService.checkVerificationCode(dto);
  }


  @MutationMapping // 사업자등록번호 유효성 검증
  public BusinessValidationCheckResponseDTO checkBizNumber(@Argument("input") BusinessValidationCheckRequestDTO dto) {
    return businessNumberCheckService.checkBusinessValidation(dto);
  }

  @MutationMapping // 최종등록
  public MemberRegisterResponseDTO register(@Argument("input") MemberRegisterRequestDTO dto) {
    return memberService.register(dto);
  }

  @QueryMapping // 내 정보 가져오기
  @PreAuthorize("isAuthenticated()") // 인증된 사람이면 전부 내 정보 가져오기 가능
  public MemberGetResponseDTO me() {
    return memberService.getMember();
  }

}
