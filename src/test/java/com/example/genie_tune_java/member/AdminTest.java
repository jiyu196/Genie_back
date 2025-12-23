package com.example.genie_tune_java.member;

import com.example.genie_tune_java.domain.admin.dto.manage_member.page.MemberSearchCondition;
import com.example.genie_tune_java.domain.admin.service.RegisterRequestService;
import com.example.genie_tune_java.domain.member.entity.Role;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class AdminTest {
  @Autowired
  RegisterRequestService registerRequestService;

  @Test
  public void getMemberList(){
    Assertions.assertNotNull(registerRequestService.findAll(1, 10, new MemberSearchCondition(null, "", RegisterStatus.PENDING, Role.MEMBER)));
    log.info(registerRequestService.findAll(1, 10, new MemberSearchCondition(null, "", RegisterStatus.PENDING, Role.MEMBER)));
  }
}
