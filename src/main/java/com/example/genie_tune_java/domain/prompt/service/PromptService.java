package com.example.genie_tune_java.domain.prompt.service;

import com.example.genie_tune_java.api.gpt.dto.OpenAIRequestDTO;
import com.example.genie_tune_java.api.gpt.dto.OpenAIResponseDTO;
import com.example.genie_tune_java.domain.prompt.dto.GenerateStoryRequestDTO;
import com.example.genie_tune_java.domain.prompt.dto.register.PreRegisterPromptDTO;
import com.example.genie_tune_java.domain.prompt.entity.Prompt;
import graphql.schema.DataFetchingEnvironment;

public interface PromptService {
  OpenAIRequestDTO checkWord (GenerateStoryRequestDTO dto, DataFetchingEnvironment env);
  Prompt finalSave(OpenAIResponseDTO dto);
}
