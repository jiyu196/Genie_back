package com.example.genie_tune_java.domain.order.dto;

import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.order.entity.OrderStatus;

public record OrderInputDTO(
  String orderUuid, Member member, OrderStatus orderStatus, Long totalAmount
) {}
