package com.example.genie_tune_java.api.nts.dto.status;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record BusinessStatusResponseDTO(
        @JsonProperty("status_code") String statusCode,
        @JsonProperty("match_cnt") int matchCnt,
        @JsonProperty("request_cnt") int requestCnt,
        List<BusinessStatusDataDTO> data
) {}
