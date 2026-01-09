package com.example.genie_tune_java.domain.prompt.dto;

import java.util.List;

public record GenerateStoryRequestDTO(
  List<String> accessIdCharacter, // 사용자 별 캐릭터의 특징을 별도로 담아서 보냄 table 저장은 아니고 그냥 파이썬 전달용
  List<String> originalContent // 사용자가 입력한 정보
) {}
