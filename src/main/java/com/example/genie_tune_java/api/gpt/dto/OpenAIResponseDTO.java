package com.example.genie_tune_java.api.gpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OpenAIResponseDTO(
  @JsonProperty("access_id")
  String accessId,
  @JsonProperty("is_slang")
  boolean isSlang,
  @JsonProperty("original_content")
  String originalContent, // Java에서 보내준 prompt
  @JsonProperty("filtered_content")
  String filteredContent, // model 거친 값 (isSlang이 false면 original과 동일)
  @JsonProperty("refined_content")
  String refinedContent, // character 묘사 앞 부분에 붙인 것
  @JsonProperty("revised_prompt")
  String revisedPrompt, // GPT 갔다온 값
  @JsonProperty("image_url")
  String imageUrl, // image URL
  @JsonProperty("error_message")
  String errorMessage // error 발생시 아마 보내주는 값인 듯
) {}
