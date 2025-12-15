package com.example.genie_tune_java.security.service;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.member.dto.MemberLoginRequestDTO;
import com.example.genie_tune_java.domain.member.dto.MemberLoginResponseDTO;
import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.member.mapper.MemberMapper;
import com.example.genie_tune_java.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {
  private final MemberRepository memberRepository;
  private final JWTService jwtService;
  private final MemberMapper memberMapper;
  private final PasswordEncoder passwordEncoder;

  public MemberLoginResponseDTO memberLogin(MemberLoginRequestDTO dto, HttpServletResponse response) throws Exception {

    // 1. 이메일 존재여부 확인

    if(memberRepository.findByEmail(dto.email()) == null){
      throw new GlobalException(ErrorCode.MEMBER_NOT_FOUND);
    }

    // 2. Member Entity 꺼내서 비밀번호 검증
    Member loginMember = memberRepository.findByEmail(dto.email());

    if(!passwordEncoder.matches(dto.password(), loginMember.getPassword())){
      throw new GlobalException(ErrorCode.MEMBER_PASSWORD_INVALID);
    }

    // 3. ResponseCookie에 JWTToken (AccessToken) 담아서 설정

    ResponseCookie accessCookie = jwtService.generateAccessTokenWithCookie(loginMember);
    response.addHeader(accessCookie.getName(), accessCookie.getValue());
    // 4. MemberLoginResponseDTO 반환 (mapper 이용해서 반환)
    return memberMapper.toLoginResponseDTO(loginMember);
  }
}
