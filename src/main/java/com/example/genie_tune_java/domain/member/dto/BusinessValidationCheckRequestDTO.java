package com.example.genie_tune_java.domain.member.dto;

// 프론트 가입 신청 창 -> 백단으로 보내주는 정보
public record BusinessValidationCheckRequestDTO(
        String bizNumber, String representativeName, String openingDate
) {}
