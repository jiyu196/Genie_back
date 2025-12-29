package com.example.genie_tune_java.domain.member.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailService {
  private final JavaMailSender mailSender;

  public void sendMail(String address, String subject, String text) throws MessagingException, UnsupportedEncodingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
    // 1. 받는 사람
    helper.setTo(address);
    // 2. 제목
    helper.setSubject(subject);
    // 3. 보내는 사람 설정 (중요!)
    // "이메일주소", "표시될이름" 순서입니다.
    helper.setFrom(new InternetAddress("genietune@gmail.com", "GenieTune"));
    // 4. 내용 (인증번호 등)
    helper.setText(text, true);
    mailSender.send(mimeMessage);
  }
}
