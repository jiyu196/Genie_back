package com.example.genie_tune_java.domain.member.dto;

import com.example.genie_tune_java.domain.member.entity.AccountStatus;
import com.example.genie_tune_java.domain.member.entity.RegisterStatus;
import com.example.genie_tune_java.domain.member.entity.Role;

import java.time.LocalDateTime;

public record MemberGetResponseDTO(
        String email, AccountStatus accountStatus, String bizNumber, String organizationName, String contactName,
        String representativeName, RegisterStatus registerStatus, Role role, LocalDateTime approvedAt,
        boolean isTempPassword, LocalDateTime passwordUpdatedAt
) {}
