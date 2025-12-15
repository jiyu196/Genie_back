package com.example.genie_tune_java.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberRegisterRequestDTO {
  @Email
  private String email;
  @NotBlank
  private String password;
  @NotBlank
  private String bizNumber;
  @NotBlank
  private String organizationName;
}
