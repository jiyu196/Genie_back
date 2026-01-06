package com.example.genie_tune_java.domain.order.dto;

import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.order.entity.OrderStatus;
import com.example.genie_tune_java.domain.product.entity.Product;

public record OrderInputDTO(
        String orderUuid, Member member, Product product, OrderStatus orderStatus, Long totalAmount
) {}
