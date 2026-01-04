package com.example.genie_tune_java.api.port_one.dto;

import com.example.genie_tune_java.domain.pay.dto.canceled.CancelledPayment;
import com.example.genie_tune_java.domain.pay.dto.failed.FailedPayment;
import com.example.genie_tune_java.domain.pay.dto.success.PaidPayment;
import com.example.genie_tune_java.domain.pay.dto.payment_method.PaymentMethod;

public record PortOneGetPaymentsResponseDTO(
  String status,
  String id,
  String transactionId,
  String merchantId,
  String storeId,
  PaymentMethod paymentMethod,
  String version,
  String requestedAt,
  String updatedAt,
  String statusChangedAt,
  String orderName,
  PaymentAmount amount,
  String currency,
  CancelledPayment cancelledPayment,
  FailedPayment failedPayment,
  PaidPayment paidPayment
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
}
