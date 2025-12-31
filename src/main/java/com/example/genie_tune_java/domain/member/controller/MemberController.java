package com.example.genie_tune_java.domain.member.controller;

import com.example.genie_tune_java.api.nts.service.BusinessNumberCheckService;
import com.example.genie_tune_java.domain.member.dto.*;
import com.example.genie_tune_java.domain.member.dto.find.FindEmailRequestDTO;
import com.example.genie_tune_java.domain.member.dto.find.FindEmailResponseDTO;
import com.example.genie_tune_java.domain.member.dto.find.ResetPasswordRequestDTO;
import com.example.genie_tune_java.domain.member.dto.find.ResetPasswordResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.MemberRegisterRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.MemberRegisterResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.biz_check.BusinessValidationCheckRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.biz_check.BusinessValidationCheckResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.email_check.EmailCheckRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.email_check.EmailCheckResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.send_code.MemberVerifyEmailRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.send_code.MemberVerifyEmailResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.verify_code.MemberVerifyCodeRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.verify_code.MemberVerifyCodeResponseDTO;
import com.example.genie_tune_java.domain.member.dto.update.*;
import com.example.genie_tune_java.domain.member.dto.withdraw.DeleteRequestDTO;
import com.example.genie_tune_java.domain.member.dto.withdraw.DeleteResponseDTO;
import com.example.genie_tune_java.domain.member.service.MemberService;
import com.example.genie_tune_java.security.util.authorize.IsMemberUser;
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


  // 1. 등록
  @MutationMapping // 이메일 인증 코드 전송
  public MemberVerifyEmailResponseDTO sendVerificationEmail(@Argument("input") MemberVerifyEmailRequestDTO dto) throws MessagingException, UnsupportedEncodingException {
    return memberService.sendVerificationCode(dto);
  }

  @MutationMapping //이메일 인증코드 검증
  public MemberVerifyCodeResponseDTO verifyCode(@Argument("input")MemberVerifyCodeRequestDTO dto) {
    return memberService.checkVerificationCode(dto);
  }

  @MutationMapping // 사업자등록번호 유효성 검증
  public BusinessValidationCheckResponseDTO checkBizNumber(@Argument("input") BusinessValidationCheckRequestDTO dto) {
    return businessNumberCheckService.checkBusinessValidation(dto);
  }
  @MutationMapping //이메일 중복검사
  public EmailCheckResponseDTO checkEmail(@Argument("input") EmailCheckRequestDTO dto) {
    return memberService.checkEmail(dto);
  }

  @MutationMapping // 최종등록
  public MemberRegisterResponseDTO register(@Argument("input") MemberRegisterRequestDTO dto) {
    return memberService.register(dto);
  }


  // 2. 조회
  // 내 정보 가져오기
  @QueryMapping
  @IsMemberUser
  public MemberGetResponseDTO me() {
    return memberService.getMember();
  }

  //이메일 찾기
  @MutationMapping
  public FindEmailResponseDTO findEmail(@Argument("input") FindEmailRequestDTO dto) {
    return memberService.findEmail(dto);
  }

  //3. 수정
  // 내 정보수정
  @MutationMapping
  @IsMemberUser
  public MemberGetResponseDTO update(@Argument("input") UpdateInfoRequestDTO dto) {
    return memberService.updateInfo(dto);
  }
  //기존 비밀번호 체크
  @MutationMapping
  @IsMemberUser
  public PasswordCheckResponseDTO checkPassword(@Argument("input") PasswordCheckRequestDTO dto) {
    return memberService.checkPassword(dto);
  }
  //비밀번호 수정
  @MutationMapping
  @IsMemberUser
  public NewPasswordResponseDTO saveNewPassword(@Argument("input") NewPasswordRequestDTO dto) {
    return memberService.saveNewPassword(dto);
  }

  //비밀번호 초기화
  @MutationMapping
  public ResetPasswordResponseDTO resetPassword(@Argument("input") ResetPasswordRequestDTO dto) throws MessagingException, UnsupportedEncodingException {
    return memberService.resetPassword(dto);
  }

  //4. 탈퇴
  @MutationMapping
  @IsMemberUser
  public DeleteResponseDTO withdraw(@Argument("input") DeleteRequestDTO dto) {
    return memberService.delete(dto);
  }
}
