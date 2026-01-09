package com.example.genie_tune_java.domain.prompt.dto.register;

import com.example.genie_tune_java.domain.prompt.entity.PromptStatus;
import com.example.genie_tune_java.domain.service_access.entity.ServiceAccess;

public record PreRegisterPromptDTO(
  ServiceAccess serviceAccess,
  String originalContent,
  PromptStatus promptStatus,
  boolean isSlang
) {}
