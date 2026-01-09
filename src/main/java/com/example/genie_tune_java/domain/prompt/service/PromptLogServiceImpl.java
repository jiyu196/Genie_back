package com.example.genie_tune_java.domain.prompt.service;

import com.example.genie_tune_java.domain.prompt.dto.register.RegisterPromptLogDTO;
import com.example.genie_tune_java.domain.prompt.mapper.PromptLogMapper;
import com.example.genie_tune_java.domain.prompt.repository.PromptLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class PromptLogServiceImpl implements PromptLogService {

  private final PromptLogRepository promptLogRepository;
  private final PromptLogMapper promptLogMapper;

  @Override
  public void register(RegisterPromptLogDTO dto) {
    promptLogRepository.save(promptLogMapper.toEntityForRegister(dto));
  }
}
