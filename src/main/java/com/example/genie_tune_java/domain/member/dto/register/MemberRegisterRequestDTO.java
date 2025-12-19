package com.example.genie_tune_java.domain.member.dto.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRegisterRequestDTO(
        @Email
        String email,
        @NotBlank
        String password,
        @NotBlank
        String bizNumber,
        @NotBlank
        String representativeName,
        @NotBlank
        String openingDate,
        @NotBlank
        String organizationName,
        @NotBlank
        String contactName
) {
}
