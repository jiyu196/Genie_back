package com.example.genie_tune_java.domain.member.service;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.common.util.RedisUtil;
import com.example.genie_tune_java.domain.admin.entity.RegisterRequest;
import com.example.genie_tune_java.domain.admin.repository.RegisterRequestRepository;
import com.example.genie_tune_java.domain.member.dto.MemberGetResponseDTO;
import com.example.genie_tune_java.domain.member.dto.find.FindEmailRequestDTO;
import com.example.genie_tune_java.domain.member.dto.find.FindEmailResponseDTO;
import com.example.genie_tune_java.domain.member.dto.find.ResetPasswordRequestDTO;
import com.example.genie_tune_java.domain.member.dto.find.ResetPasswordResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.MemberRegisterRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.MemberRegisterResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.send_code.MemberVerifyEmailRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.send_code.MemberVerifyEmailResponseDTO;
import com.example.genie_tune_java.domain.member.dto.register.verify_code.MemberVerifyCodeRequestDTO;
import com.example.genie_tune_java.domain.member.dto.register.verify_code.MemberVerifyCodeResponseDTO;
import com.example.genie_tune_java.domain.member.dto.update.*;
import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.member.mapper.MemberMapper;
import com.example.genie_tune_java.domain.member.repository.MemberRepository;
import com.example.genie_tune_java.security.dto.JWTPrincipal;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

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
    member.updatePassword(encodedPassword, LocalDateTime.now());
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

  @Transactional
  public MemberGetResponseDTO updateInfo(UpdateInfoRequestDTO dto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    JWTPrincipal principal = (JWTPrincipal) authentication.getPrincipal();
    Member member =  memberRepository.findById(principal.getMemberId()).orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
    member.changeInfo(dto.representativeName(), dto.contactName());
    return memberMapper.toMemberGetResponseDTO(member);
  }

  @Override
  @Transactional(readOnly = true)
  public OldPasswordCheckResponseDTO checkPassword(OldPasswordCheckRequestDTO dto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    JWTPrincipal principal = (JWTPrincipal) authentication.getPrincipal();
    Member member = memberRepository.findById(principal.getMemberId()).orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

    if(!passwordEncoder.matches(dto.oldPassword(), member.getPassword())){
      throw new GlobalException(ErrorCode.MEMBER_PASSWORD_INVALID);
    }
    return new OldPasswordCheckResponseDTO(true);
  }

  @Override
  @Transactional
  public NewPasswordResponseDTO saveNewPassword(NewPasswordRequestDTO dto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    JWTPrincipal principal = (JWTPrincipal) authentication.getPrincipal();
    Member member = memberRepository.findById(principal.getMemberId()).orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

    // 새로 입력한 비밀번호가 기존 비밀번호와 같은 경우 Exception 투척
    if(passwordEncoder.matches(dto.newPassword(), member.getPassword())){
      throw new GlobalException(ErrorCode.SAME_OLD_PASSWORD);
    }
    // 엔티티 메서드로 update 시키기 (member 가입시랑 같은 password)
    member.updatePassword(passwordEncoder.encode(dto.newPassword()), LocalDateTime.now());
    member.checkIsTempPassword(false);

    return new NewPasswordResponseDTO(true);
  }

  @Override
  @Transactional(readOnly = true)
  public FindEmailResponseDTO findEmail(FindEmailRequestDTO dto) {

    Member member = memberRepository.findByBizNumberAndContactName(dto.bizNumber(), dto.contactName()).orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

    String maskedEmail = maskEmail(member.getEmail());
    return new FindEmailResponseDTO(maskedEmail);

  }

  private String maskEmail(String email) {
    if(email == null || !email.contains("@")){
      return email;
    }
    //@를 기준으로 2개를 문자 배열로 나눔
    String[] parts = email.split("@");

    // genie@genietune.com 이 있다고 가정
    // 문자열의 0번 인덱스 genie
    String id = parts[0];
    //문자열의 1번 인덱스 genietune.com
    String domain = parts[1];
    //문자열을 객체를 새로 생성하지 않고 만드는 Builder 생성
    StringBuilder maskedId = new StringBuilder();

    if (id.length() <= 2) {
      // ab@naver.com -> a*@naver.com
      return maskedId.append(id.charAt(0)).append("*").append("@").append(domain).toString();
    }

    // 앞 2글자만 남기고 나머지는 전부 *로 채움 (글자 수 유지)
    // abcd@naver.com -> ab**@naver.com
    // genie@naver.com -> ge***@naver.com
    return maskedId.append(id, 0, 2).append("*".repeat(id.length() - 2)).append("@").append(domain).toString();
  }

  @Override
  @Transactional
  public ResetPasswordResponseDTO resetPassword(ResetPasswordRequestDTO dto) throws MessagingException, UnsupportedEncodingException {
    //1. 3중 대조 쿼리문
    Member member = memberRepository.findByEmailAndBizNumberAndContactName(dto.email(), dto.bizNumber(),  dto.contactName()).orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

    //2. 임시 비밀번호 생성 및 암호화 저장
    String tempPw = UUID.randomUUID().toString().substring(0, 10);

    member.updatePassword(passwordEncoder.encode(tempPw), LocalDateTime.now());
    member.checkIsTempPassword(true);
    mailService.sendMail(dto.email(), "임시비밀번호는: " + tempPw + " 입니다.");

    return new ResetPasswordResponseDTO(true);
  }
}
