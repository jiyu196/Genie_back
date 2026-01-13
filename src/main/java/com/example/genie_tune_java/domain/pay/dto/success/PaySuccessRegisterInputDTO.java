package com.example.genie_tune_java.domain.pay.dto.success;

import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.order.entity.Order;
import com.example.genie_tune_java.domain.pay.entity.PayStatus;

import java.time.OffsetDateTime;

public record PaySuccessRegisterInputDTO(
        Order order, // 지불한 주문 정보
        Member member, // 지불한 사람
        PayStatus payStatus,
        Long amount,
        String transactionId,
        OffsetDateTime paidAt, // 지불 시점
        String pgTxId, // PG사 거래 아이디 -> tbl_pay 기록
        String pgResponse, // PG사 거래 응답 본문
        String receiptUrl, // 거래 영수증 URL
        // 결제 정보에 필요한 수단 2가지 추가
        String pgType,
        String cardCompany
) {}
