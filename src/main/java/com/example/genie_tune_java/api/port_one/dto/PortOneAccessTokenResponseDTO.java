package com.example.genie_tune_java.api.port_one.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PortOneAccessTokenResponseDTO(
  @JsonProperty("accessToken")
  String accessToken,
  @JsonProperty("refreshToken")
  String refreshToken
) {}
