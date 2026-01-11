package com.example.genie_tune_java.domain.attach.dto;

import com.example.genie_tune_java.domain.attach.entity.AttachTargetType;

public record AttachRequestDTO(
  AttachTargetType attachTargetType,
  Long targetId
) {}
