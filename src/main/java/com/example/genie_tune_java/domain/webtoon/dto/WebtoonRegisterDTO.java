package com.example.genie_tune_java.domain.webtoon.dto;

import com.example.genie_tune_java.domain.prompt.entity.Prompt;
import com.example.genie_tune_java.domain.webtoon.entity.WebtoonStatus;

public record WebtoonRegisterDTO(
  String title,
  Prompt prompt,
  WebtoonStatus webtoonStatus,
  String webtoonGroupId
) {}
