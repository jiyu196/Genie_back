package com.example.genie_tune_java.domain.member.dto;

import com.example.genie_tune_java.domain.member.entity.AccountStatus;
import com.example.genie_tune_java.domain.member.entity.RegisterStatus;

public record MemberRegisterResponseDTO(
   Long id, String email, String bizNumber, String organizationName, RegisterStatus registerStatus, AccountStatus accountStatus
) {}
