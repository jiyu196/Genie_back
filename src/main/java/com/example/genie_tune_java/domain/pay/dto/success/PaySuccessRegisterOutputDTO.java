package com.example.genie_tune_java.domain.pay.dto.success;

import com.example.genie_tune_java.domain.pay.entity.PayStatus;

public record PaySuccessRegisterOutputDTO(
  PayStatus payStatus, // 결제 상태
  String paidAt
) {}
