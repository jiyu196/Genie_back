package com.example.genie_tune_java.domain.member.dto;

import com.example.genie_tune_java.domain.member.entity.AccountStatus;
import com.example.genie_tune_java.domain.member.entity.RegisterStatus;
import com.example.genie_tune_java.domain.member.entity.Role;

public record MemberLoginResponseDTO(
        String email, AccountStatus accountStatus, String bizNumber, String organizationName, RegisterStatus registerStatus, Role role
) {
}
