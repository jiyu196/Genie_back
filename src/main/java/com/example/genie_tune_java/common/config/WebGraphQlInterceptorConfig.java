package com.example.genie_tune_java.common.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class WebGraphQlInterceptorConfig implements WebGraphQlInterceptor {
  //GraphQlContext에 내가 Custom한 속성 값을 주입하기 위해 사용하는 설정

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
              if(response != null) {
              contextBuilder.put(HttpServletResponse.class, response);
              }

              if(req != null) {
              contextBuilder.put(HttpServletRequest.class, req);
              }
      });
      return builder.build();
    });
    return chain.next(request);
  }
}
