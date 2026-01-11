package com.example.genie_tune_java.domain.attach.dto;

import com.example.genie_tune_java.domain.attach.entity.AttachTargetType;

public record AttachRegisterDTO(
  AttachTargetType attachTargetType,
  Long targetId,
  String s3Key,
  String fileName,
  String originName,
  String extension,
  String contentType,
  Long fileSize
) {}
