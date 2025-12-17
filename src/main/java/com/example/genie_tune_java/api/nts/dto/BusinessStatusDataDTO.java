package com.example.genie_tune_java.api.nts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BusinessStatusDataDTO {
  @JsonProperty("b_no")
  private String bNo;                   // 사업자번호

  @JsonProperty("b_stt")
  private String bStt;                  // 사업자 상태 (계속사업자 등)

  @JsonProperty("b_stt_cd")
  private String bSttCd;                // 상태 코드

  @JsonProperty("tax_type")
  private String taxType;               // 과세 유형

  @JsonProperty("tax_type_cd")
  private String taxTypeCd;             // 과세 유형 코드

  @JsonProperty("end_dt")
  private String endDt;                 // 폐업일자

  @JsonProperty("utcc_yn")
  private String utccYn;                // 단위과세전환폐업 여부

  @JsonProperty("tax_type_change_dt")
  private String taxTypeChangeDt;       // 과세유형 변경일

  @JsonProperty("invoice_apply_dt")
  private String invoiceApplyDt;        // 세금계산서 적용일

  @JsonProperty("rbf_tax_type")
  private String rbfTaxType;            // 직전 과세유형

  @JsonProperty("rbf_tax_type_cd")
  private String rbfTaxTypeCd;          // 직전 과세유형 코드
}
