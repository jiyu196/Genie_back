package com.example.genie_tune_java.common.config;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.graphql.server.webmvc.GraphQlHttpHandler;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RequestPredicates.contentType;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class GraphQlConfig {

  @Bean
  public RuntimeWiringConfigurer runtimeWiringConfigurer() {
    return wiringBuilder -> wiringBuilder.scalar(uploadScalar());
  }
  @Bean
  public GraphQLScalarType uploadScalar() {
    return GraphQLScalarType.newScalar()
            .name("Upload") // schema.graphqls에 쓴 이름과 정확히 같아야 함
            .description("GraphQL에 File Type이 없어서 신규 정의")
            .coercing(new Coercing<Object, Object>() {
              @Override
              public MultipartFile serialize(Object dataFetcherResult) {
                return (MultipartFile) dataFetcherResult;
              }

              @Override
              public MultipartFile parseValue(Object input) {
                return (MultipartFile) input;
              }

              @Override
              public MultipartFile parseLiteral(Object input) {
                return null; // 파일은 쿼리 텍스트로 전달될 수 없으므로 null
              }
            })
            .build();
  }


}
