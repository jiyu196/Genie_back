package com.example.genie_tune_java.domain.admin.dto.manage_member;

import com.example.genie_tune_java.domain.member.entity.RegisterStatus;

public record JoinApplyRequestDTO(
        String email, RegisterStatus registerStatus, String rejectReason
) {}
