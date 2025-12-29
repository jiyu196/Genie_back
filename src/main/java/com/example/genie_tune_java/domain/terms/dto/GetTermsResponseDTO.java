package com.example.genie_tune_java.domain.terms.dto;

import com.example.genie_tune_java.domain.terms.entity.TermsStatus;

import java.time.LocalDateTime;

public record GetTermsResponseDTO(
  Integer version, String title, String content, boolean isRequired, LocalDateTime registeredAt, TermsStatus termsStatus
) {}
