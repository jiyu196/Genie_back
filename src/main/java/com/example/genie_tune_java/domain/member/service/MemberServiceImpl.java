package com.example.genie_tune_java.domain.member.service;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.member.dto.MemberRegisterRequestDTO;
import com.example.genie_tune_java.domain.member.dto.MemberRegisterResponseDTO;
import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.member.mapper.MemberMapper;
import com.example.genie_tune_java.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {
  private final MemberRepository memberRepository;
  private final MemberMapper memberMapper;
  private final PasswordEncoder passwordEncoder;
  @Override
  public MemberRegisterResponseDTO register(MemberRegisterRequestDTO dto) {

    //1. 프론트 단에서 막겠지만, 한 번 더 이메일 중복 체크
    if(memberRepository.existsByEmail(dto.getEmail())) {
      throw new GlobalException(ErrorCode.EMAIL_DUPLICATED);
    }
    //2. 사업자 등록증 중복 체크
    if(memberRepository.existsByOrganizationName(dto.getOrganizationName())) {
      throw new GlobalException(ErrorCode.ORGANIZATION_NAME_DUPLICATED);
    }
    //3. 사업자 등록증 번호 중복 체크 필요..한가?

    Member member = memberMapper.registerMember(dto);
    String encodedPassword = passwordEncoder.encode(member.getPassword());
    member.savePassword(encodedPassword);
    memberRepository.save(member);

    return memberMapper.toRegisterResponseDTO(member);
  }
}
