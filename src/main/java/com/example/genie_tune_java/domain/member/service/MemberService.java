package com.example.genie_tune_java.domain.member.service;

import com.example.genie_tune_java.domain.member.dto.MemberGetResponseDTO;
import com.example.genie_tune_java.domain.member.dto.find.FindEmailRequestDTO;
import com.example.genie_tune_java.domain.member.dto.find.FindEmailResponseDTO;
import com.example.genie_tune_java.domain.member.dto.find.ResetPasswordRequestDTO;
import com.example.genie_tune_java.domain.member.dto.find.ResetPasswordResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.MemberRegisterRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.MemberRegisterResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.email_check.EmailCheckRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.email_check.EmailCheckResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.send_code.MemberVerifyEmailRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.send_code.MemberVerifyEmailResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.verify_code.MemberVerifyCodeRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.verify_code.MemberVerifyCodeResponseDTO;
import com.example.genie_tune_java.domain.member.dto.update.*;
import com.example.genie_tune_java.domain.member.dto.withdraw.DeleteRequestDTO;
import com.example.genie_tune_java.domain.member.dto.withdraw.DeleteResponseDTO;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface MemberService {
  // Register
  MemberRegisterResponseDTO register(MemberRegisterRequestDTO dto);
  // 1) 등록시 & 비밀번호 찾기시 초기화하는 메일인증 코드 보내기
  MemberVerifyEmailResponseDTO sendVerificationCode(MemberVerifyEmailRequestDTO dto) throws MessagingException, UnsupportedEncodingException;
  // 2) 인증 코드 확인 요청
  MemberVerifyCodeResponseDTO checkVerificationCode(MemberVerifyCodeRequestDTO dto);
  // 3) 이메일 중복 체크
  EmailCheckResponseDTO checkEmail(EmailCheckRequestDTO dto);
  //Get
  // 조회 (단일 멤버)
  MemberGetResponseDTO getMember();

  //Update
  // 1) 멤버 정보 수정(대표자 명, 담당자 명)
  MemberGetResponseDTO updateInfo(UpdateInfoRequestDTO dto);
  // 2) 기존 비밀번호 check
  PasswordCheckResponseDTO checkPassword(PasswordCheckRequestDTO dto);
  // 3) 새 비밀번호 변경
  NewPasswordResponseDTO saveNewPassword(NewPasswordRequestDTO dto);
  //Delete 필요

  //findEmail
  FindEmailResponseDTO findEmail(FindEmailRequestDTO dto);

  //resetPassword
  ResetPasswordResponseDTO resetPassword(ResetPasswordRequestDTO dto) throws MessagingException, UnsupportedEncodingException;

  //withdraw
  DeleteResponseDTO delete(DeleteRequestDTO dto);


}
