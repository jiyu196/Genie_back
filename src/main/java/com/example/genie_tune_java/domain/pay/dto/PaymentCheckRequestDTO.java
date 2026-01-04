package com.example.genie_tune_java.domain.pay.dto;

public record PaymentCheckRequestDTO(
  String paymentId, // 결제 요청할 때 만들어놓은 OrderUuid
  String transactionType, // "PAYMENT" 로 온다.
  String txId //
) {}
