package com.example.genie_tune_java.domain.order.dto;

import com.example.genie_tune_java.domain.order.entity.OrderStatus;

public record MakeOrderResponseDTO(
  String orderUuid, OrderStatus orderStatus, Long totalAmount, String createdAt
) {}
