package com.example.genie_tune_java.domain.member.dto.register.biz_check;

public record BusinessValidationCheckResponseDTO(
  String bizNumber, boolean validation, String validationMsg, String businessStatus, String businessStatusCode
) {}
