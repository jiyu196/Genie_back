package com.example.genie_tune_java.common.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class WebGraphQlInterceptorConfig implements WebGraphQlInterceptor {
  //GraphQlContext에 내가 Custom한 속성 값을 주입하기 위해 사용하는 설정

  @Override
  public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {

    request.configureExecutionInput((executionInput, builder) -> {
      HttpServletResponse response =
              (HttpServletResponse) request.getAttributes().get(HttpServletResponse.class.getName());

      builder.graphQLContext(contextBuilder ->
              contextBuilder.put(HttpServletResponse.class, response)
      );
      return builder.build();
    });
    return chain.next(request);
  }
}
