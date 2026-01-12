package com.example.genie_tune_java.domain.webtoon.dto.page;

import java.util.List;

public record WebtoonGroupResponseDTO(
  String webtoonGroupId,
  String title,
  List<WebtoonCutDTO> cuts // { } 안에 리스트 형태로 포함
) {
  // 내부(Inner) 레코드로 정의해서 응집도를 높임
  public record WebtoonCutDTO(
          String imageUrl
  ) {}
}
