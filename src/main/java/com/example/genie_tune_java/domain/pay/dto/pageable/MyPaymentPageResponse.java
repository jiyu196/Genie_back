package com.example.genie_tune_java.domain.pay.dto.pageable;

import java.util.List;

public record MyPaymentPageResponse(
        List<GetPaymentResponseDTO> content, // 실제 데이터 리스트
        int totalPages,                          // 전체 페이지 수
        long totalElements,                       // 전체 데이터 개수
        int currentPage,                          // 현재 페이지 번호
        boolean isFirst,                          // 첫 페이지 여부
        boolean isLast
) {}