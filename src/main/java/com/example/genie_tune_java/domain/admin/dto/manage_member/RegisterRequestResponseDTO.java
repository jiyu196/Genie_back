package com.example.genie_tune_java.domain.admin.dto.manage_member;

import com.example.genie_tune_java.domain.member.entity.AccountStatus;
import com.example.genie_tune_java.domain.member.entity.RegisterStatus;
import com.example.genie_tune_java.domain.member.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestResponseDTO {
  // --- Member(계정) 관련 정보 ---
  private String email;
  private String organizationName;
  private String bizNumber;
  private String representativeName;
  private String contactName;
  private Role role;
  private AccountStatus accountStatus;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime approvedAt;

  // --- RegisterRequest(신청 건) 관련 정보 ---
  private String rejectReason;
  private RegisterStatus registerStatus;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime checkedAt;

  // --- 파일 정보 (저장된 경로) ---
  private String businessLicenseUrl;
  private String employmentCertUrl;

  public void insertAttach(String businessLicenseUrl, String employmentCertUrl) {
    this.businessLicenseUrl = businessLicenseUrl;
    this.employmentCertUrl = employmentCertUrl;
  }
}
