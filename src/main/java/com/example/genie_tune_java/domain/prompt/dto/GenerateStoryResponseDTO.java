package com.example.genie_tune_java.domain.prompt.dto;

public record GenerateStoryResponseDTO(
  String originalContent,
  String refinedContent,
  String revisedPrompt,
  String imageUrl,
  String errorMessage
) {}
