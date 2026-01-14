package com.example.genie_tune_java.domain.webtoon.dto.page;

public record MyPageWebtoonRequestDTO(
  int page,
  int size,
  String decryptedKey
) {}
