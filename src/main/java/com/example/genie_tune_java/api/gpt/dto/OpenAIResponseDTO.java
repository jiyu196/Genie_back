package com.example.genie_tune_java.api.gpt.dto;

import java.util.List;

public record OpenAIResponseDTO(
        List<Choice> choices,
        Usage usage
) {
  public record Choice(
          Message message, String finish_reason
  ) {
    public record Message(
            String role,
            String content
    ) {}
  }

  public record Usage(
          int prompt_tokens,
          int completion_tokens,
          int total_tokens
  ) {}
}
