package com.example.genie_tune_java.api.port_one.dto;

import com.example.genie_tune_java.domain.pay.dto.canceled.CancelledPayment;
import com.example.genie_tune_java.domain.pay.dto.failed.FailedPayment;
import com.example.genie_tune_java.domain.pay.dto.success.PaidPayment;
import com.example.genie_tune_java.domain.pay.dto.payment_method.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public record PortOneGetPaymentsResponseDTO(
  String status,
  String id,
  String transactionId,
  String merchantId,
  String storeId,
  @JsonProperty("method")
  PaymentMethod paymentMethod,
  String version,
  OffsetDateTime requestedAt,
  OffsetDateTime updatedAt,
  OffsetDateTime statusChangedAt,
  String orderName,
  PaymentAmount amount,
  String currency,
  //상태가 CancelledPayment 일 때
//  String paidAt, 성공시와 필드가 겹침
//  String pgTxId, 성공시와 필드가 겹침
//  String receiptUrl, 성공시와 필드가 겹침
  ArrayList<PaymentCancellation> cancellations,
  //상태가 PaymentFailure 일 때
  OffsetDateTime failedAt,
  PaymentFailure paymentFailure,
  //상태가 PaidPayment 일 때
  OffsetDateTime paidAt, // 지불 시점
  String pgTxId, // PG사 거래 아이디 -> tbl_pay 기록
  String pgResponse, // PG사 거래 응답 본문
  String receiptUrl // 거래 영수증 URL
) {
  public record PaymentAmount(
          Long total,
          Long taxFree,
          Long vat,
          Long supply,
          Long discount,
          Long paid,
          Long cancelled,
          Long cancelledTaxFree
  ) {}

  public record PaymentFailure(
          String reason,
          String pgCode,
          String pgMessage
  ) {}

  public record PaymentCancellation(
          String status,
          String id,
          String pgCancellationId,
          Long totalAmount,
          Long taxFreeAmount,
          Long vatAmount,
          String reason,
          OffsetDateTime cancelledAt,
          OffsetDateTime requestedAt
  ) {}
}
