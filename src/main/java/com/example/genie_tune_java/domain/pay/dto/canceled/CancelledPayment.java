package com.example.genie_tune_java.domain.pay.dto.canceled;

import java.util.ArrayList;

public record CancelledPayment(
  String paidAt,
  String pgTxId,
  String receiptUrl,
  ArrayList<PaymentCancellation> cancellations
) {
  public record PaymentCancellation(
    String status,
    String id,
    String pgCancellationId,
    Long totalAmount,
    Long taxFreeAmount,
    Long vatAmount,
    String reason,
    String cancelledAt,
    String requestedAt
  ) {}
}
