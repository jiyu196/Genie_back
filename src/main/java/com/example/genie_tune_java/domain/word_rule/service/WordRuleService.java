package com.example.genie_tune_java.domain.word_rule.service;

import com.example.genie_tune_java.domain.word_rule.dto.GetWordRulesResponseDTO;

import java.util.List;

public interface WordRuleService {
  List<GetWordRulesResponseDTO> getWordRules();
}
