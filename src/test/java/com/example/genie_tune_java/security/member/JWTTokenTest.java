package com.example.genie_tune_java.security.member;

import com.example.genie_tune_java.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class JWTTokenTest {

  @Autowired
  JWTUtil jwtUtil;

//  @Test
//  public void generateJWTTokenTest() {
//    log.info(jwtUtil);
//    Assertions.assertNotNull(jwtUtil);
//    String accessToken = jwtUtil.createAccessToken(1L, Role.MEMBER.toString());
//    log.info(accessToken);
//    Assertions.assertNotNull(accessToken);
//  }

//  @Test
//  public void getClaimFromJWTTokenTest() {
//    String accessToken = jwtUtil.createAccessToken(1L, Role.MEMBER.toString());
//    TokenStatus status = jwtUtil.checkToken(accessToken);
//    Assertions.assertEquals(TokenStatus.VALID, status);
//    log.info(status);
//  }
}
