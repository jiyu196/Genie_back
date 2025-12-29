package com.example.genie_tune_java.mail;

import com.example.genie_tune_java.domain.member.service.MailService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;

@SpringBootTest
public class MailTest {
  @Autowired
  MailService mailService;

  @Test
  public void sendMail() throws MessagingException, UnsupportedEncodingException {
    mailService.sendMail("chankim94@naver.com", "인증코드 발급해드립니다.","554321");
  }
}
