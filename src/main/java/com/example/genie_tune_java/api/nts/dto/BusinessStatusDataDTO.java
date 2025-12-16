package com.example.genie_tune_java.api.nts.dto;

import lombok.Getter;

@Getter
public class BusinessStatusDataDTO {
  private String b_no;                  // 사업자번호
  private String b_stt;                 // 사업자 상태 (계속사업자 등)
  private String b_stt_cd;              // 상태 코드
  private String tax_type;              // 과세 유형
  private String tax_type_cd;           // 과세 유형 코드
  private String end_dt;                // 폐업일자
  private String utcc_yn;               // 단위과세전환폐업 여부
  private String tax_type_change_dt;    // 과세유형 변경일
  private String invoice_apply_dt;      // 세금계산서 적용일
  private String rbf_tax_type;           // 직전 과세유형
  private String rbf_tax_type_cd;        // 직전 과세유형 코드
}
