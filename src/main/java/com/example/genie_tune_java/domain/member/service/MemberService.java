package com.example.genie_tune_java.domain.member.service;

import com.example.genie_tune_java.domain.member.dto.MemberGetResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.MemberRegisterRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.MemberRegisterResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.send_code.MemberVerifyEmailRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.send_code.MemberVerifyEmailResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.verify_code.MemberVerifyCodeRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.verify_code.MemberVerifyCodeResponseDTO;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface MemberService {
  MemberRegisterResponseDTO register(MemberRegisterRequestDTO dto);
  public MemberGetResponseDTO getMember();
  public MemberVerifyEmailResponseDTO sendVerificationCode(MemberVerifyEmailRequestDTO dto) throws MessagingException, UnsupportedEncodingException;
  public MemberVerifyCodeResponseDTO checkVerificationCode(MemberVerifyCodeRequestDTO dto);

}
