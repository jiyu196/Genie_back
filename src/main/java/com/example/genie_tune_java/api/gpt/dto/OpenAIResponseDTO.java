package com.example.genie_tune_java.api.gpt.dto;

import java.util.List;

public record OpenAIResponseDTO(
  String accessId,
  boolean isSlang
) {}
