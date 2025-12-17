package com.example.genie_tune_java.domain.member.service;

import com.example.genie_tune_java.domain.member.dto.MemberGetResponseDTO;
import com.example.genie_tune_java.domain.member.dto.MemberRegisterRequestDTO;
import com.example.genie_tune_java.domain.member.dto.MemberRegisterResponseDTO;
import com.example.genie_tune_java.domain.member.entity.Member;

public interface MemberService {
  MemberRegisterResponseDTO register(MemberRegisterRequestDTO dto);
  public MemberGetResponseDTO getMember();
}
