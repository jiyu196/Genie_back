package com.example.genie_tune_java.domain.attach.dto;

import com.example.genie_tune_java.domain.attach.entity.AttachTargetType;

import java.time.LocalDateTime;

public record AttachResponseDTO(
  AttachTargetType attachTargetType,
  Long targetId, // Member Webtoon

  String originName,
  String contentType,
  Long fileSize,

  String fileUrl,
  LocalDateTime createdAt
) {}
