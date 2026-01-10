package com.example.genie_tune_java.domain.attach.dto;

import com.example.genie_tune_java.domain.attach.entity.TargetType;

public record AttachRequestDTO(
  TargetType targetType,
  Long targetId
) {}
