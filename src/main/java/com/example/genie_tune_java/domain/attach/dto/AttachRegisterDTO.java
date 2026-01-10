package com.example.genie_tune_java.domain.attach.dto;

import com.example.genie_tune_java.domain.attach.entity.TargetType;

public record AttachRegisterDTO(
  TargetType targetType,
  Long targetId,
  String s3Key,
  String fileName,
  String originName,
  String extension,
  String contentType,
  Long fileSize
) {}
