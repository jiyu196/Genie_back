package com.example.genie_tune_java.api.nts.dto.validation;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BusinessValidationInputData(
  @JsonProperty("b_no") String bizNumber,
  @JsonProperty("start_dt") String openingDate,
  @JsonProperty("p_nm") String representativeName,
  @JsonProperty("p_nm2") String representativeName2,
  @JsonProperty("b_nm") String businessName,
  @JsonProperty("corp_no") String corpNumber,
  @JsonProperty("b_sector") String businessSector,
  @JsonProperty("b_type") String businessType,
  @JsonProperty("b_adr") String businessAddress
) {}
