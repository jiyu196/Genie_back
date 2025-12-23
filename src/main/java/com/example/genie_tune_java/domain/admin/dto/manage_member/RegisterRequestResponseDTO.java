package com.example.genie_tune_java.domain.admin.dto.manage_member;

import com.example.genie_tune_java.domain.member.entity.AccountStatus;
import com.example.genie_tune_java.domain.member.entity.RegisterStatus;
import com.example.genie_tune_java.domain.member.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

//외부 리스트나 get 용도의 ResponseDTO
public record RegisterRequestResponseDTO(
  //Member에서 필요한 정보
  String email, String organizationName, String bizNumber, String representativeName,
  String contactName, Role role, AccountStatus accountStatus,
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime approvedAt,
  //기존 RegisterRequest에 등록된 정보 중 필요한 정보
  String rejectReason, RegisterStatus registerStatus,
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime checkedAt
) {}
