package com.example.genie_tune_java.domain.member.dto;

public record BusinessValidationCheckResponseDTO(
  String bizNumber, String organizationName, boolean validation, String validationMsg
) {}
