package com.example.genie_tune_java.domain.pay.dto;

public record PaymentCheckResponseDTO(
        String orderUuid,         // 주문 고유 번호 (조회/CS용)
        String displayName,       // 구매한 상품명 (예: 프리미엄 100)
        Long amount,         // 실제 결제된 금액
        String payStatus,            // 결제 상태 (예: PAID, READY 등)
        int issuedAccessCount,    // 방금 발급된 슬롯 개수 (예: 30)
        String message,           // 사용자에게 보여줄 간단한 메시지
        String paidAt      // 결제 완료 시간
) {}
