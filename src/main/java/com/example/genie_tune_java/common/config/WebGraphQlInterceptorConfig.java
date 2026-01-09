package com.example.genie_tune_java.common.config;

import com.example.genie_tune_java.common.dto.ServiceAccessIdPrincipal;
import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.common.util.RedisUtil;
import com.example.genie_tune_java.domain.service_access.entity.AccessStatus;
import com.example.genie_tune_java.security.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;

@Component
@Log4j2
@RequiredArgsConstructor
public class WebGraphQlInterceptorConfig implements WebGraphQlInterceptor {
  //GraphQlContext에 내가 Custom한 속성 값을 주입하기 위해 사용하는 설정
  private final CookieUtil cookieUtil;
  private final RedisUtil redisUtil;
  private final static Set<String> checkPoint = Set.of(
    "GenerateStory", "savePrompt"
  );

  @Override
  public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
    //수동으로 GraphQL 뮤테이션/쿼리 문 실행할 때 Context의 수동으로 넣어주는 로직
    // ExecutionInput 내에 (Query, Variables, Context 등)
    request.configureExecutionInput((executionInput, builder) -> {
      //GraphQL Context에 수동으로 심을 친구들 (HttpServletResponse 및 Request 등)

      ServletRequestAttributes reqAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

      HttpServletResponse res = reqAttributes.getResponse();
      HttpServletRequest req = reqAttributes.getRequest();

      String operationName = request.getOperationName();

      log.info("mutation/query 명: {}", operationName);

      log.info("Response: {} Request: {}", res, req);

      builder.graphQLContext(contextBuilder -> {
        contextBuilder.put(HttpServletRequest.class, req);
        if (res != null) {
          contextBuilder.put(HttpServletResponse.class, res);
        }
        ServiceAccessIdPrincipal principal = validateAndGetPrincipal(operationName, req);
        if(principal != null) {
          log.info("accessId: {} accessStatus: {} 만료기간: {}" , principal.getAccessId(), principal.getAccessStatus(), principal.getExpiredAt());
          contextBuilder.put(ServiceAccessIdPrincipal.class, principal);
        }
      });
      return builder.build();
    });

    return chain.next(request);
  }

  private ServiceAccessIdPrincipal validateAndGetPrincipal(String operationName, HttpServletRequest request) {

    if(operationName == null || !isSecurityTarget(operationName)) {
      return null;
    }

    log.info("validation 진입");

    //1. Session Cookie에서 발급한 UUID 꺼냄
    String sessionCookieValue = cookieUtil.getCookieValue(request, "Service_Access_Cookie");

    //2. 쿠키 value 값 추출 (null 일때 throw)
    if(sessionCookieValue == null) {
      throw new GlobalException(ErrorCode.SERVICE_ACCESS_REQUIRED);
    }

    //3. redis 내 value 값 검증
    String redisValue = redisUtil.get("SCAI:" + sessionCookieValue);
    if(redisValue == null) {
      throw new GlobalException(ErrorCode.SERVICE_ACCESS_RELOGIN_REQUIRED);
    }

    // 3-1. redis 내 value의 TTL 값 확인
    long ttl = redisUtil.getTtl("SCAI:" + sessionCookieValue);

    if(ttl <= 3 * 60 * 1000 && ttl > 0) { //redis는 key가 없으면 ttl이 -2, ttl이 없으면 -1로 반환한다.
      redisUtil.expire("SCAI:" + sessionCookieValue, 20 * 60 * 1000);
    }

    //4. 성공하면 value 값 문자열 파싱(split) -> 아마 이전에 신규 로직 필요할 듯
    //ACTIVE:SAID-1a2b3c4(accessId 번호):2026-02-06T12:00:00 형태로 value 값 생성 예정
    String[] parts = redisValue.split(":", 3); // LocalDateTime 뒤의 : 는
    Arrays.stream(parts).forEach(log::info);
    //5. 문자열 오류시 에러 던짐
    if(parts.length != 3) {
      throw new GlobalException(ErrorCode.SERVICE_ACCESS_MALFORMED);
    }
    //6. :을 기준으로 파싱
    AccessStatus accessStatus = AccessStatus.valueOf(parts[0]);
    String accessId = String.valueOf(parts[1]);
    LocalDateTime expireTime = LocalDateTime.parse(parts[2]);

    if(accessStatus != AccessStatus.ACTIVE || expireTime.isBefore(LocalDateTime.now())) {
      throw new GlobalException(ErrorCode.PAYMENT_REQUIRED);
    }

    return new ServiceAccessIdPrincipal(accessId, accessStatus, expireTime);

  }

  private boolean isSecurityTarget(String operationName) {
    return checkPoint.contains(operationName);
  }

}
