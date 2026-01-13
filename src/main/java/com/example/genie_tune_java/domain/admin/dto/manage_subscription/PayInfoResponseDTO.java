package com.example.genie_tune_java.domain.admin.dto.manage_subscription;

import com.example.genie_tune_java.domain.pay.entity.PayStatus;

public record PayInfoResponseDTO(
  String organizationName, // 멤버 정보에서 가져옴
  PayStatus payStatus,
  Long amount,
  String pgType,
  String cardCompany,
  String reason, // 실패나 취소 했을 시 사유를 받아오고자 하는 column,
  //상품정보
  String displayName
) {}
