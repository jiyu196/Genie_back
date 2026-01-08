package com.example.genie_tune_java.domain.prompt.service;

import com.example.genie_tune_java.common.config.WordRuleCache;
import com.example.genie_tune_java.domain.prompt.repository.PromptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class PromptServiceImpl implements PromptService {
  private final PromptRepository promptRepository;
  private final WordRuleCache wordRuleCache;


}
