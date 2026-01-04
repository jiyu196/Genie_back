package com.example.genie_tune_java.api.port_one.dto;

public record PortOneGetPaymentsRequestDTO(
  String paymentId,
  String storeId // secret값에 있는 StoreId
) {}
