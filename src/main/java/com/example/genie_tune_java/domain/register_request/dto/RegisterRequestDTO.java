package com.example.genie_tune_java.domain.register_request.dto;

import com.example.genie_tune_java.domain.register_request.entity.RegisterRequestStatus;

public record RegisterRequestDTO(
        String bizNumber, String businessStatus, String businessStatusCode, RegisterRequestStatus registerRequestStatus
) {}
