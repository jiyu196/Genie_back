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
import java.util.Set;

@Component
@Log4j2
@RequiredArgsConstructor
public class WebGraphQlInterceptorConfig implements WebGraphQlInterceptor {
  //GraphQlContext에 내가 Custom한 속성 값을 주입하기 위해 사용하는 설정
  private final CookieUtil cookieUtil;
  private final RedisUtil redisUtil;
  private final static Set<String> checkPoint = Set.of(
    "generateImage", "savePrompt"
  );

  @Override
  public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
    //수동으로 GraphQL 뮤테이션/쿼리 문 실행할 때 Context의 수동으로 넣어주는 로직
    // ExecutionInput 내에 (Query, Variables, Context 등)

    request.configureExecutionInput((executionInput, builder) -> {
      //GraphQL Context에 수동으로 심을 친구들 (HttpServletResponse 및 Request 등)

      ServletRequestAttributes reqAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

      HttpServletResponse response = reqAttributes.getResponse();
      HttpServletRequest req = reqAttributes.getRequest();

      log.info("Response: {} Request: {}", response, req);
      builder.graphQLContext(contextBuilder -> {
        if (response != null) {
          contextBuilder.put(HttpServletResponse.class, response);
        }
        contextBuilder.put(HttpServletRequest.class, req);
      });
      return builder.build();
    });
    return chain.next(request);
  }

  private ServiceAccessIdPrincipal validateAndGetPrincipal(String operationName, HttpServletRequest request) {
    if(operationName == null || !isSecurityTarget(operationName)) {
      return null;
    }
    //1. Session Cookie에서 salt 친 serviceAccessId 꺼냄
    String sessionCookieValue = cookieUtil.getCookieValue(request, "Service_Access_Cookie");
    String redisValue = redisUtil.get("SA:" + sessionCookieValue);
    //ACTIVE:7(MEMBER 번호):2026-02-06T12:00:00 형태로 value 값 생성 예정
    String[] parts = redisValue.split(":");

    if(parts.length != 3) {
      throw new GlobalException(ErrorCode.SERVICE_ACCESS_MALFORMED);
    }
    AccessStatus accessStatus = AccessStatus.valueOf(parts[0]);
    Long memberId = Long.valueOf(parts[1]);
    LocalDateTime expireTime = LocalDateTime.parse(parts[2]);

    if(accessStatus != AccessStatus.ACTIVE || expireTime.isBefore(LocalDateTime.now())) {
      throw new GlobalException(ErrorCode.PAYMENT_REQUIRED);
    }

    return new ServiceAccessIdPrincipal(memberId, accessStatus, expireTime);

  }

  private boolean isSecurityTarget(String operationName) {
    return checkPoint.contains(operationName);
  }

}
