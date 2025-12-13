package com.example.genie_tune_java.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //@PreAuthorize 가능 (컨트롤러 단에서 Resolver로 권한 조정)
public class SecurityConfig {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http    // graphql + jwt + 세션사용안함
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            //csrf 꺼버리기
            .csrf(csrf -> csrf.disable())

            .httpBasic(httpBasicAuth->httpBasicAuth.disable())

            //내가 만든 Filter 등록(JWTAuthentication Filter) before 나 after로

            // Http Request(API 요청과 관련하여, graphql 접근 요청은 전부 허용하고 개별 controller에서 확인
            .authorizeHttpRequests(auth ->
            auth
                    .requestMatchers("/graphql").permitAll()
                    .requestMatchers("/graphiql").permitAll()
                    .anyRequest().denyAll() // Rest API 엔드포인트 차단
    );

    return http.build();
  }
}
