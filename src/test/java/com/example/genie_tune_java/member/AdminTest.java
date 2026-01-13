package com.example.genie_tune_java.member;

import com.example.genie_tune_java.domain.admin.service.AdminService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class AdminTest {
  @Autowired
  AdminService adminService;

  @Test
  public void getMemberList(){
//    Assertions.assertNotNull(registerRequestService.findAll(1, 10, new MemberSearchCondition(null, "", RegisterStatus.PENDING, Role.MEMBER)));
//    log.info(registerRequestService.findAll(1, 10, new MemberSearchCondition(null, "", RegisterStatus.PENDING, Role.MEMBER)));
  }
}
