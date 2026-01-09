package com.example.genie_tune_java.domain.prompt.dto.register;

import com.example.genie_tune_java.domain.prompt.entity.Prompt;
import com.example.genie_tune_java.domain.prompt.entity.SourceType;

public record RegisterPromptLogDTO(
  Prompt prompt,
  String reason,
  String errorMessage,
  String originalWord,
  String filteredWord,
  SourceType sourceType
) {}
