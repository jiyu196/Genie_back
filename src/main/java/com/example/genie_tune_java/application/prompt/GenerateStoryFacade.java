package com.example.genie_tune_java.application.prompt;

import com.example.genie_tune_java.api.gpt.dto.OpenAIRequestDTO;
import com.example.genie_tune_java.api.gpt.dto.OpenAIResponseDTO;
import com.example.genie_tune_java.api.gpt.service.OpenAIService;
import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.prompt.dto.GenerateStoryRequestDTO;
import com.example.genie_tune_java.domain.prompt.dto.GenerateStoryResponseDTO;
import com.example.genie_tune_java.domain.prompt.service.PromptService;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class GenerateStoryFacade {

  private final PromptService promptService;
  private final OpenAIService openAIService;

  @Transactional
  public GenerateStoryResponseDTO generateWebtoon(GenerateStoryRequestDTO dto, DataFetchingEnvironment env) {
    //1. 파이썬에 집어넣을 DTO 생성
    OpenAIRequestDTO openAIRequestDTO = promptService.checkWord(dto, env);
    //2. 파이썬에서 보내준 결과값
    OpenAIResponseDTO openAIResponseDTO = openAIService.requestPythonProcessing(openAIRequestDTO);
    // 혹시나 imageUrl 이면 실패임
    if(openAIResponseDTO.imageUrl() == null || openAIResponseDTO.imageUrl().isEmpty()) {
      log.error("AI 이미지 생성 실패 : {} ", openAIResponseDTO.errorMessage());
      throw new GlobalException(ErrorCode.IMAGE_GENERATION_FAILED);
    }
    log.info("image URL: {}", openAIResponseDTO.imageUrl());

    // 프롬프트 최종 저장 (엔티티 메서드로 활용)
    promptService.finalSave(openAIResponseDTO);

    return new GenerateStoryResponseDTO(
            openAIResponseDTO.originalContent(), openAIResponseDTO.refinedContent(), openAIResponseDTO.revisedPrompt(),
            openAIResponseDTO.imageUrl(), openAIResponseDTO.errorMessage());
  }
}
