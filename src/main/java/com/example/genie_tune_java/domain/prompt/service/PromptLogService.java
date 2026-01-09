package com.example.genie_tune_java.domain.prompt.service;

import com.example.genie_tune_java.domain.prompt.dto.register.RegisterPromptLogDTO;

public interface PromptLogService {

  void register(RegisterPromptLogDTO dto);
}
