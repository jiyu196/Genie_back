package com.example.genie_tune_java.domain.admin.dto.manage_subscription;
import com.example.genie_tune_java.domain.pay.entity.PayStatus;

public record SalesSearchCondition(
  //1. 검색관련
  SalesSearchType salesSearchType, // 검색조건
  String keyword, //keyword
  //2. 정렬 필터관련
  PayStatus payStatus, // 결제 상태 -> tbl_pay
  String pgType, // 결제 수단 (카드, 계좌이체 등) -> tbl_pay_method
  String cardCompany,        // KB, SHINHAN, HYUNDAI 등 tbl_paymethod
  String from, // 기간 시작점
  String to, // 기간 종료점
  String displayName
) {}
