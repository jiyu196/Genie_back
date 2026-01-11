package com.example.genie_tune_java.domain.member.service;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.common.util.RedisUtil;
import com.example.genie_tune_java.domain.admin.entity.RegisterRequest;
import com.example.genie_tune_java.domain.admin.repository.RegisterRequestRepository;
import com.example.genie_tune_java.domain.attach.dto.AttachRequestDTO;
import com.example.genie_tune_java.domain.attach.entity.AttachTargetType;
import com.example.genie_tune_java.domain.attach.service.AttachService;
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
import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.member.mapper.MemberMapper;
import com.example.genie_tune_java.domain.member.repository.MemberRepository;
import com.example.genie_tune_java.domain.terms.entity.MemberTerms;
import com.example.genie_tune_java.domain.terms.entity.Terms;
import com.example.genie_tune_java.domain.terms.entity.TermsCategory;
import com.example.genie_tune_java.domain.terms.mapper.MemberTermsMapper;
import com.example.genie_tune_java.domain.terms.repository.MemberTermsRepository;
import com.example.genie_tune_java.domain.terms.repository.TermsRepository;
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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {
  private final MemberRepository memberRepository;
  private final MemberMapper memberMapper;
  // 가입 요청을 위한 Repository
  private final RegisterRequestRepository registerRequestRepository;
  // 카테고리를 통해 약관 가져오는 로직
  private final TermsRepository termsRepository;
  // 가져온 약관을 만든 Member와 함께 관계형 테이블 객체 (MemberTerms) 로 만들기 위해 필요한 과정
  private final MemberTermsRepository memberTermsRepository;
  private final MemberTermsMapper memberTermsMapper;
  // 비밀번호 인코딩
  private final PasswordEncoder passwordEncoder;
  // mail 보내는 로직
  private final MailService mailService;
  // mail 인증 코드 로직
  private final RedisUtil redisUtil;
  //첨부파일 저장 로직
  private final AttachService attachService;

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

    //6. 약관 동의 목록 가져오기
    List<Terms> agreedTerms = dto.agreedTermsCategory().stream().map(category -> termsRepository.findLatestByCategory(Enum.valueOf(TermsCategory.class, category.toUpperCase())).orElseThrow(() -> new GlobalException(ErrorCode.TERMS_NOT_FOUND))).toList();
    
    //7. MemberTerms 객체 만들기 및 저장
    List<MemberTerms> memberTerms = agreedTerms.stream().map(terms -> memberTermsMapper.toEntity(member, terms)).toList();

    //8. JPA에서 리스트 형태로 저장하는 방법 (해당 객체 일괄 테이블에 저장)
    memberTermsRepository.saveAll(memberTerms);

    //9. 사업자등록증/재직증명서 첨부파일 저장
    // 9-1. 사업자등록증 upload
    AttachRequestDTO businessDto = new AttachRequestDTO(AttachTargetType.MEMBER_BUSINESS, member.getId());
    AttachRequestDTO employDto = new AttachRequestDTO(AttachTargetType.MEMBER_EMPLOYMENT, member.getId());

    attachService.upload(businessDto, dto.businessFile());
    // 9-2. 재직증명서 upload
    attachService.upload(employDto, dto.employmentFile());

    //10. 회원가입하고 프론트에 뿌릴 정보 들을 담은 DTO로 변환 및 반환
    return memberMapper.toRegisterResponseDTO(member);
  }

  public MemberGetResponseDTO getMember() {
    Member loginMember = findMember();
    return memberMapper.toMemberGetResponseDTO(loginMember);
  }
  //이메일 중복여부 추가 필요


  @Override
  @Transactional(readOnly = true)
  public EmailCheckResponseDTO checkEmail(EmailCheckRequestDTO dto) {
    if(memberRepository.existsByEmail(dto.email())) {
      throw new GlobalException(ErrorCode.EMAIL_DUPLICATED);
    }
    return new EmailCheckResponseDTO(true);
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
    String subject = "GenieTune 회원가입을 위한 인증코드 6자리를 보내드립니다.";
    String text = "인증 코드 : " + authCode + "\n 5분 이내로 인증하여 주시기 바랍니다.";

    mailService.sendMail(email, subject, text);

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

    Member member = findMember();
    member.changeInfo(dto.representativeName(), dto.contactName());
    return memberMapper.toMemberGetResponseDTO(member);
  }

  @Override
  @Transactional(readOnly = true)
  public PasswordCheckResponseDTO checkPassword(PasswordCheckRequestDTO dto) {

    Member member = findMember();

    if(!passwordEncoder.matches(dto.password(), member.getPassword())){
      throw new GlobalException(ErrorCode.MEMBER_PASSWORD_INVALID);
    }
    return new PasswordCheckResponseDTO(true);
  }

  @Override
  @Transactional
  public NewPasswordResponseDTO saveNewPassword(NewPasswordRequestDTO dto) {

    Member member = findMember();
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
    mailService.sendMail(dto.email(), "GenieTune 임시비밀번호 발급","임시비밀번호는: " + tempPw + " 입니다.");

    return new ResetPasswordResponseDTO(true);
  }

  @Override
  @Transactional
  public DeleteResponseDTO delete(DeleteRequestDTO dto) {
    //프론트 단에서 입력한 삭제하겠습니다. 메세지가 일치하지 않는 경우 에러 던지기
    if(!dto.message().equals("삭제하겠습니다")) {
      throw new GlobalException(ErrorCode.DELETE_MESSAGE_INVALID);
    }
    log.info("delete 서비스 로직 진입");
    //인증 객체에서 로그인한 Member 정보 꺼내서 entity 메서드로 member 정보 수정! (DirtyChecking)
    Member member = findMember();
    log.info("Member 찾음");
    member.softDelete();
    log.info("softDelete 진행, {} {}", member.getDeletedAt(), member.getAccountStatus());
    return new DeleteResponseDTO(true);
  }

  // 인증 객체에서 Member 객체 단일로 가져오는 메서드(private)
  private Member findMember() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    JWTPrincipal principal = (JWTPrincipal) authentication.getPrincipal();
    return memberRepository.findById(principal.getMemberId()).orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
  }
}
