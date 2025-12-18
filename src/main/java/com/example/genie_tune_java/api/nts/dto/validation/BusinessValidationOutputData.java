package com.example.genie_tune_java.api.nts.dto.validation;

import com.example.genie_tune_java.api.nts.dto.status.BusinessStatusDataDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public record BusinessValidationOutputData(
        @JsonProperty("b_no") String bizNumber,
        @JsonProperty("valid") String validationCode, // 유효한지 내려주는 값 01 valid 02 invalid
        @JsonProperty("valid_msg") String resultMessage,
        @JsonProperty("request_param") BusinessValidationInputData requestParam,
        @JsonProperty("status")BusinessStatusDataDTO status
        ) {}
