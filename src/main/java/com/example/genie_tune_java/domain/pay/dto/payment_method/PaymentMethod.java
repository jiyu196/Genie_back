package com.example.genie_tune_java.domain.pay.dto.payment_method;

public record PaymentMethod(
  String type,
  PaymentMethodCard paymentMethodCard
) {
  public record PaymentMethodCard(
    Card card,
    String approvalNumber
  ) {
    public record Card(
      String publisher,
      String issuer,
      String number
    ){}
  }
}
