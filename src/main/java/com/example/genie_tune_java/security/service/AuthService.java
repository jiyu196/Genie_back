package com.example.genie_tune_java.security.service;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.common.util.RedisUtil;
import com.example.genie_tune_java.domain.member.dto.login.MemberLoginRequestDTO;
import com.example.genie_tune_java.domain.member.dto.login.MemberLoginResponseDTO;
import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.member.repository.MemberRepository;
import com.example.genie_tune_java.domain.service_access.dto.login.ServiceAccessLoginRequestDTO;
import com.example.genie_tune_java.domain.service_access.dto.login.ServiceAccessLoginResponseDTO;
import com.example.genie_tune_java.domain.service_access.entity.AccessStatus;
import com.example.genie_tune_java.domain.service_access.entity.ServiceAccess;
import com.example.genie_tune_java.domain.service_access.repository.ServiceAccessRepository;
import com.example.genie_tune_java.security.util.AESUtil;
import com.example.genie_tune_java.security.util.CookieUtil;
import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {
  private final MemberRepository memberRepository;
  private final JWTService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final CookieUtil cookieUtil;
  private final RedisUtil redisUtil;
  private final AESUtil aesUtil;
  private final ServiceAccessRepository serviceAccessRepository;

  @Transactional(readOnly = true)
  public MemberLoginResponseDTO memberLogin(MemberLoginRequestDTO dto, DataFetchingEnvironment env) throws Exception {

    // 1. 이메일 존재여부 확인
    Member loginMember = memberRepository.findByEmail(dto.email()).orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

    // 2. Member Entity 꺼내서 비밀번호 검증

    if(!passwordEncoder.matches(dto.password(), loginMember.getPassword())){
      throw new GlobalException(ErrorCode.MEMBER_PASSWORD_INVALID);
    }

    // 3. ResponseCookie에 JWTToken (AccessToken, RefreshToken) 각각 담아서 설정

    ResponseCookie accessCookie = jwtService.generateAccessTokenWithCookie(loginMember);
    String randomUuid = UUID.randomUUID().toString();
    ResponseCookie refreshCookie = jwtService.generateRefreshTokenWithCookie(randomUuid);
    //4. RefreshToken 설정 값을 Redis에 넣기
    long ttlSeconds = refreshCookie.getMaxAge().getSeconds();
    //인증 객체 생성시 필요한 pk와 role 값을 redis에서 꺼내기
    String redisValue = loginMember.getId().toString() + ":" + loginMember.getRole().toString();

    redisUtil.set("RT:" + randomUuid, redisValue, ttlSeconds * 1000); // 여기 TTL을 쿠키 시간과 동일하게

    HttpServletResponse response = env.getGraphQlContext().get(HttpServletResponse.class);

    response.addHeader("Set-Cookie", accessCookie.toString());
    response.addHeader("Set-Cookie", refreshCookie.toString());
    // 4. 성공 가정 true 반환

    return new MemberLoginResponseDTO(true);
  }


  public Boolean memberLogout(DataFetchingEnvironment env) {
    HttpServletRequest request = env.getGraphQlContext().get(HttpServletRequest.class);
    String refreshToken = cookieUtil.getCookieValue(request, "Refresh_Cookie");
    if(refreshToken == null){
      log.warn("refreshToken이 null, 강제 로그아웃 처리합니다.");
      ResponseCookie deleteAccessCookie = cookieUtil.deleteCookie("Access_Cookie");
      HttpServletResponse response = env.getGraphQlContext().get(HttpServletResponse.class);
      response.addHeader("Set-Cookie", deleteAccessCookie.toString());
      return true;
    }
    String refreshRandomUuid = jwtService.getSubject(refreshToken);

    log.info("logout 요청 진입");
    ResponseCookie deleteAccessCookie = cookieUtil.deleteCookie("Access_Cookie");
    ResponseCookie deleteRefreshCookie = cookieUtil.deleteCookie("Refresh_Cookie");
    log.info("AccessCookie: {} RefreshCookie: {}",deleteAccessCookie, deleteRefreshCookie);
    HttpServletResponse response = env.getGraphQlContext().get(HttpServletResponse.class);
    response.addHeader("Set-Cookie", deleteAccessCookie.toString());
    response.addHeader("Set-Cookie", deleteRefreshCookie.toString());
    redisUtil.delete("RT:" + refreshRandomUuid);
    return true;
  }

  @Transactional
  public ServiceAccessLoginResponseDTO serviceAccessLogin(ServiceAccessLoginRequestDTO dto, DataFetchingEnvironment env) {
    // 0. 기존에 sessiionCookie 있을 경우 삭제 로직 sessionCookie는 새로 발급해야함/ redis도 삭제
    // service access id 하나는 단 하나의 세션만 유지
    HttpServletRequest request = env.getGraphQlContext().get(HttpServletRequest.class);
    HttpServletResponse response = env.getGraphQlContext().get(HttpServletResponse.class);
    String cookieValue = cookieUtil.getCookieValue(request, "Service_Access_Cookie");

    if(cookieValue != null) {
      redisUtil.delete("SCAI:" + cookieValue);
      // response.addHeader("Set-Cookie", cookieUtil.deleteCookie("Service_Access_Cookie").toString()); 이 부분은 필요가 없다.
    }

    // 1. 최초 originalKey 원문
    String decodedKey = aesUtil.decrypt(dto.encryptedKey());
    // 2. email 개념의 access_id 추출
    String accessId = decodedKey.substring(0, 12);
    // 3. Service Access 객체 생성
    ServiceAccess serviceAccess = serviceAccessRepository.findByAccessId(accessId).orElseThrow(() -> new GlobalException(ErrorCode.SERVICE_ACCESS_NOTFOUND));
    // 4. 비밀번호 (passwordEncoder를 통해 만들어진 비밀번호)
    String accessHash = serviceAccess.getAccessHash();
    // 5. 비밀번호 검증
    if(!passwordEncoder.matches(decodedKey, accessHash)) {
      throw new GlobalException(ErrorCode.SERVICE_ACCESS_INVALID);
    }
    // 6. serviceAccess의 status check ACTIVE  아니면 결제 pending 중이거나 만료된거임
    if(serviceAccess.getAccessStatus() != AccessStatus.ACTIVE) {
      throw new GlobalException(ErrorCode.PAYMENT_REQUIRED);
    }

    // 7. 구독 만료 시간 check
    if(serviceAccess.getExpiredAt().isBefore(LocalDateTime.now())) {
      throw new GlobalException(ErrorCode.SUBSCRIPTION_EXPIRED);
    }

    // 8. 랜덤 uuid 발급
    String randomUuid = UUID.randomUUID().toString();
    ResponseCookie serviceAccessCookie = cookieUtil.createServiceAccessCookie(randomUuid);

    // Redis TTL = session lifetime
    // redis value에서 serviceAccess expiredAt = subscription 의 유효기간
    // 9. redis에 uuid 저장 -> SessionCookieAccessId라는 뜻의 SCAI
    // 쿠키는 기본적으로 name과 domain, path가 동일하므로 새로 만들어도 덮어씌워지는 구조다. -> 즉, 한 브라우저 내에서는 한 개의 accessId만 허용
    redisUtil.set("SCAI:" + randomUuid, serviceAccess.getAccessStatus() + ":" + serviceAccess.getMember().getId() + ":" + serviceAccess.getExpiredAt(), 15 * 60 * 1000L);

    // 10. response header에 만든 세션쿠키 저장

    response.addHeader("Set-Cookie", serviceAccessCookie.toString());

    return new ServiceAccessLoginResponseDTO (true);
  }

  public boolean serviceAccessLogout(DataFetchingEnvironment env) {

    HttpServletRequest request = env.getGraphQlContext().get(HttpServletRequest.class);
    String randomUuid = cookieUtil.getCookieValue(request, "Service_Access_Cookie");

    redisUtil.delete("SCAI:" + randomUuid);
    log.info("serviceAccessLogout 요청 진입!");
    HttpServletResponse response = env.getGraphQlContext().get(HttpServletResponse.class);

    response.addHeader("Set-Cookie", cookieUtil.deleteCookie("Service_Access_Cookie").toString());

    return true;
  }


}
