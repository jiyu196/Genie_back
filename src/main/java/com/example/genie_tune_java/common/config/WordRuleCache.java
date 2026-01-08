package com.example.genie_tune_java.common.config;

import com.example.genie_tune_java.domain.word_rule.dto.GetWordRulesResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class WordRuleCache {

  private volatile List<GetWordRulesResponseDTO> cache = List.of();

  public void load(List<GetWordRulesResponseDTO> data) {
    this.cache = List.copyOf(data); // 불변화 (중요)
    log.info("WordRuleCache loaded. size={}", cache.size());
  }

  public List<GetWordRulesResponseDTO> getAll() {
    return cache;
  }
}
