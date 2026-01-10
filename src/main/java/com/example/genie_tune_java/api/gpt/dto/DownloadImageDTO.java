package com.example.genie_tune_java.api.gpt.dto;

public record DownloadImageDTO(
  byte[] bytes,
  String contentType,
  long fileSize
) {}
