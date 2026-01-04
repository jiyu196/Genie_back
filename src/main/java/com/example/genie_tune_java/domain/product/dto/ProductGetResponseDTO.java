package com.example.genie_tune_java.domain.product.dto;

public record ProductGetResponseDTO(
  Long id,
  int price, // 상품 가격
  int duration, // 상품 구독 기간
  int maxPromptDailyCount, // 일 max 프롬프트 사용량
  int maxWebtoonStorage,  //max webtoon 저장 용량
  int maxServiceAccessIdCount, // 상품 등급에 따라 차등 발급 되는 Service Access Id 갯수 (Default: 30개)
  String productStatus, // 상품 상태(현재 판매중, 삭제됨, 이용불가 등)
  String productGrade, // 상품 등급(BASIC, PRO, PREMIUM 등)
  String subscriptionCycle, // 구독 주기(월간, 연간)
  String displayName, // 화면에 보일 상품명
  String description //상품 상세 설명
) {}
