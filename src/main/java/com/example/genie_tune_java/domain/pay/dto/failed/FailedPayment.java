package com.example.genie_tune_java.domain.pay.dto.failed;

public record FailedPayment(
  String failedAt,
  PaymentFailure paymentFailure
) {
  public record PaymentFailure(
    String reason,
    String pgCode,
    String pgMessage
  ) {}
}
