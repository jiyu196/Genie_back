package com.example.genie_tune_java.domain.subscription.dto;

import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.order.entity.Order;
import com.example.genie_tune_java.domain.pay.entity.PayMethod;
import com.example.genie_tune_java.domain.product.entity.Product;

import java.time.LocalDateTime;

public record SubscriptionRegisterInputDTO(
  Member member, // 구독 회원
  Order order, // 최초 주문서
  Product product, // 구독한 플랜
  PayMethod payMethod //구독 당시 결제 수단
) {}
