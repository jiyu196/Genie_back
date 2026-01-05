package com.example.genie_tune_java.domain.order.dto;

import com.example.genie_tune_java.domain.order.entity.OrderStatus;

public record MakeOrderResponseDTO(
  String orderUuid, //주문번호 OR+UUID 형태로 발급
  OrderStatus orderStatus, //주문 상태 PENDING, SUCCESS, CANCELED 3개
  Long totalAmount, // 총 결제 금액
  String createdAt, // 주문 생성시각
  String organizationName, // 주문자 정보(기관명)
  String storeId,
  Long productId,
  String displayName
) {}
