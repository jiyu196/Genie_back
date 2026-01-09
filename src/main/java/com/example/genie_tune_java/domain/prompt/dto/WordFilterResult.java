package com.example.genie_tune_java.domain.prompt.dto;

public record WordFilterResult(
  String originalWord,
  String filteredWord,
  boolean isSlang,
  String reason
) {
  public String resolveWord() {
    return isSlang ? filteredWord : originalWord;
  }
}
