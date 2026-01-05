package com.example.genie_tune_java.domain.pay.dto.payment_method;

import com.example.genie_tune_java.domain.member.entity.Member;

public record PayMethodRegisterInputDTO(
  Member member,
  String pgType, // 결제 종류
  String cardCompany, //카드사
  String cardNumberMask // 마스킹 된 카드번호
) {}
