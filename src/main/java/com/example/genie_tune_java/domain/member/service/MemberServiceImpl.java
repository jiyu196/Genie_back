package com.example.genie_tune_java.domain.member.service;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.common.util.RedisUtil;
import com.example.genie_tune_java.domain.admin.entity.RegisterRequest;
import com.example.genie_tune_java.domain.admin.repository.RegisterRequestRepository;
import com.example.genie_tune_java.domain.member.dto.MemberGetResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.MemberRegisterRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.MemberRegisterResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.send_code.MemberVerifyEmailRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.send_code.MemberVerifyEmailResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.verify_code.MemberVerifyCodeRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.verify_code.MemberVerifyCodeResponseDTO;
import com.example.genie_tune_java.domain.member.dto.update.UpdateInfoRequestDTO;
import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.member.mapper.MemberMapper;
import com.example.genie_tune_java.domain.member.repository.MemberRepository;
import com.example.genie_tune_java.security.dto.JWTPrincipal;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {
  private final MemberRepository memberRepository;
  private final RegisterRequestRepository registerRequestRepository;
  private final MemberMapper memberMapper;
  private final PasswordEncoder passwordEncoder;
  private final MailService mailService;
  private final RedisUtil redisUtil;

  @Override
  @Transactional
  public MemberRegisterResponseDTO register(MemberRegisterRequestDTO dto) {

    //1. 프론트 단에서 막겠지만, 한 번 더 이메일 중복 체크
    if(memberRepository.existsByEmail(dto.email())) {
      throw new GlobalException(ErrorCode.EMAIL_DUPLICATED);
    }
    //2. 사업자 단체명 중복 체크
    if(memberRepository.existsByOrganizationName(dto.organizationName())) {
      throw new GlobalException(ErrorCode.ORGANIZATION_NAME_DUPLICATED);
    }
    //3. 사업자 등록번호 중복 체크
    if(memberRepository.existsByBizNumber(dto.bizNumber())) {
      throw new GlobalException(ErrorCode.BIZ_NUMBER_DUPLICATED);
    }
    //4. Member Entity 객체 생성 및 db 등록
    Member member = memberMapper.registerMember(dto);
    String encodedPassword = passwordEncoder.encode(dto.password());
    log.info(encodedPassword);
    member.updatePassword(encodedPassword);
    memberRepository.save(member);

    //5. Member Register Request Entity 객체 생성 및 db 등록
    registerRequestRepository.save(RegisterRequest.createRequest(member));

    return memberMapper.toRegisterResponseDTO(member);
  }

  public MemberGetResponseDTO getMember() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    JWTPrincipal principal = (JWTPrincipal) authentication.getPrincipal();
    log.info("principal :{}, getMemberId: {}",principal, principal.getMemberId());
    Member loginMember = memberRepository.findById(principal.getMemberId()).orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
    return memberMapper.toMemberGetResponseDTO(loginMember);
  }

  public MemberVerifyEmailResponseDTO sendVerificationCode(MemberVerifyEmailRequestDTO dto) throws MessagingException, UnsupportedEncodingException {
    //이메일 추출
    String email = dto.email();
    String type= dto.type();

    if (redisUtil.hasKey(type + ":" + email)) {
      redisUtil.delete(type + ":" + email);
    }

    String authCode = String.format("%06d", (int) (Math.random() * 1000000));

    redisUtil.set(type + ":" + email, authCode, 300000L); // 5분

    log.info("저장된 Code:{}", redisUtil.get(type + ":" + email));

    String text = "인증 코드 : " + authCode + "\n 5분 이내로 인증하여 주시기 바랍니다.";

    mailService.sendMail(email, text);

    return new MemberVerifyEmailResponseDTO(true);
  }

  public MemberVerifyCodeResponseDTO checkVerificationCode(MemberVerifyCodeRequestDTO dto) {
    String email = dto.email();
    String code = dto.code();
    String type = dto.type();
    log.info(email);
    log.info( "이메일: {} 입력한 코드: {} 메일 타입: {}", email, code, type);
    String savedCode = redisUtil.get(type + ":" + email);
    log.info("저장된 코드: {}", savedCode);
    if(savedCode == null){
      throw new GlobalException(ErrorCode.CODE_EXPIRED);
    }
    if(!savedCode.equals(code)){
      throw new GlobalException(ErrorCode.CODE_INVALID);
    }
    redisUtil.delete(type + ":" + email);
    return new MemberVerifyCodeResponseDTO(true);
  }

  public MemberGetResponseDTO updateInfo(UpdateInfoRequestDTO dto) {
    return null;
  }

}
