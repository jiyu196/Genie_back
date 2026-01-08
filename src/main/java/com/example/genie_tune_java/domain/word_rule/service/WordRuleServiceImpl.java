package com.example.genie_tune_java.domain.word_rule.service;

import com.example.genie_tune_java.domain.word_rule.dto.GetWordRulesResponseDTO;
import com.example.genie_tune_java.domain.word_rule.entity.WordRule;
import com.example.genie_tune_java.domain.word_rule.mapper.WordRuleMapper;
import com.example.genie_tune_java.domain.word_rule.repository.WordRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class WordRuleServiceImpl implements WordRuleService {
  private final WordRuleRepository wordRuleRepository;
  private final WordRuleMapper wordRuleMapper;

  @Transactional(readOnly = true)
  public List<GetWordRulesResponseDTO> getWordRules() {
    List<WordRule> wordRuleList = wordRuleRepository.findAll();

    return wordRuleList.stream().map(wordRuleMapper::toListDTO).toList();
  }
}
