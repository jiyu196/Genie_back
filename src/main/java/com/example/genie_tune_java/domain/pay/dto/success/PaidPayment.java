package com.example.genie_tune_java.domain.pay.dto.success;

public record PaidPayment(
  String paidAt, // 지불 시점
  String pgTxId, // PG사 거래 아이디 -> tbl_pay 기록
  String pgResponse, // PG사 거래 응답 본문
  String receiptUrl // 거래 영수증 URL
) {}
