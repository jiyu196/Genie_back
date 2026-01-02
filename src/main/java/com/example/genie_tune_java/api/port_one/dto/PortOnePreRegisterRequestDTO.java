package com.example.genie_tune_java.api.port_one.dto;

public record PortOnePreRegisterRequestDTO( // paymentId는 Order객체의 order-uuid로 대체함
  String storeId,      // 포트원 상점 ID
  long totalAmount,    // 결제 예정 금액
  long taxFreeAmount,  // 면세 금액 (없으면 0)
  String currency      // "KRW"
) {}
