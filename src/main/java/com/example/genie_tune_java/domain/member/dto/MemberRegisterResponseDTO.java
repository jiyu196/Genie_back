package com.example.genie_tune_java.domain.member.dto;

import com.example.genie_tune_java.domain.member.entity.AccountStatus;
import com.example.genie_tune_java.domain.member.entity.RegisterStatus;

import java.time.LocalDateTime;

public record MemberRegisterResponseDTO(
        Long id, String email, String bizNumber, String organizationName, String contactName,
        LocalDateTime registeredAt, RegisterStatus registerStatus, AccountStatus accountStatus
) {}
