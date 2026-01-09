package com.example.genie_tune_java.domain.prompt.dto.register;

import com.example.genie_tune_java.domain.prompt.entity.PromptStatus;

public record UpdatePromptDTO(
  String filteredContent,
  String refinedContent,
  String revisedContent,
  PromptStatus promptStatus
) {}
