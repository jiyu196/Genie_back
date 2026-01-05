package com.example.genie_tune_java.domain.pay.dto.success;

import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.order.entity.Order;
import com.example.genie_tune_java.domain.pay.entity.PayStatus;

public record PaySuccessRegisterInputDTO(
  Order order, // 지불한 주문 정보
  Member member, // 지불한 사람
  PayStatus payStatus,
  Long amount,
  PaidPayment paidPayment
) {}
