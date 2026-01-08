package com.example.genie_tune_java.domain.pay.dto.pageable;

import com.example.genie_tune_java.domain.pay.entity.PayStatus;

import java.time.LocalDateTime;

public record GetPaymentResponseDTO(
        String orderUuid, // member id -> order 접근
        Long amount, // pay (결제 금액)
        LocalDateTime paidAt, // updatedAt
        PayStatus payStatus, // payStatus
        //추가 정보 1 (카드정보)
        String cardCompany, // member id -> payMethod
        String cardNumberMask, // member id -> payMethod
        //추가 정보 2 (영수증 정보)
        String receiptUrl // pay 정보 보유
) {

}
