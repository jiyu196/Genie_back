package com.example.genie_tune_java.domain.member.dto.register.verify_code;

public record MemberVerifyCodeRequestDTO(
        String email, String code, String type
) {}
