package com.example.genie_tune_java.security;

import com.example.genie_tune_java.common.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class JWTTokenTest {

  @Autowired
  JWTUtil jwtUtil;

  @Test
  public void generateJWTTokenTest() {
    log.info(jwtUtil);
    Assertions.assertNotNull(jwtUtil);
    String accessToken = jwtUtil.createAccessToken(1L);
    log.info(accessToken);
    Assertions.assertNotNull(accessToken);
  }
}
