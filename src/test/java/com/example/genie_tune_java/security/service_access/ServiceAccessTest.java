package com.example.genie_tune_java.security.service_access;

import com.example.genie_tune_java.domain.service_access.repository.ServiceAccessRepository;
import com.example.genie_tune_java.security.util.AESUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class ServiceAccessTest {
  @Autowired
  private ServiceAccessRepository serviceAccessRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private AESUtil aesUtil;

  @Test
  @DisplayName("구독 ID 1번에 대해 서비스 액세스 키 30개 일괄 생성")
  public void serviceAccessDummyTest(){

  }
}
