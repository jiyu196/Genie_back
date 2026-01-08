package com.example.genie_tune_java.domain.word_rule.dto;

public record GetWordRulesResponseDTO(
  String forbiddenWord,
  String cleanWord,
  String reason
) {}
