package com.example.genie_tune_java.common.exception;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Log4j2
public class GlobalGraphQLExceptionResolver extends DataFetcherExceptionResolverAdapter {

  @Override
  protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
    //1. 발생 예외가 내가 만든 GlobalException인지 check
    if (ex instanceof GlobalException globalException) {
      // 2. 내부 ErrorCode 추출
      ErrorCode errorCode = globalException.getErrorCode();
      // 3. GraphQL 응답 형식인 GraphQLError 생성 및 반환 (HTTP는 거의 200으로 반환하므로 에러 정보를 별도로 담아야 함
      return GraphQLError.newError()
              .message(errorCode.getMessage()) // errorCode 내 Message 반환 (사전 Enum에 정의)
              .path(env.getExecutionStepInfo().getPath()) //GraphQL 쿼리의 계층 구조 내에서 현재 처리중인 필드의 위치와 컨텍스트 표시
              .extensions(Map.of(
                      "errorCode", errorCode.name(),
                      "HttpStatus", errorCode.getStatus().value(),
                      "message", errorCode.getMessage()
              ))
              .errorType(ErrorClassification.errorClassification(errorCode.name()))
              .build();
    }
    return  super.resolveToSingleError(ex, env);
  }
}
