package com.example.genie_tune_java.api.nts.dto.validation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record BusinessValidationResponseDTO(
  @JsonProperty("status_code") String statusCode,
  @JsonProperty("request_cnt") int requestCount,
  @JsonProperty("valid_cnt") int validCount,
  List<BusinessValidationOutputData> data
) {
}
