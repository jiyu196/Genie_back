package com.example.genie_tune_java.domain.prompt.controller;

import com.example.genie_tune_java.application.prompt.GenerateStoryFacade;
import com.example.genie_tune_java.domain.prompt.dto.GenerateStoryRequestDTO;
import com.example.genie_tune_java.domain.prompt.dto.GenerateStoryResponseDTO;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Log4j2
public class PromptController {

  private final GenerateStoryFacade generateStoryFacade;

  @MutationMapping
  public GenerateStoryResponseDTO generateStory(@Argument("input") GenerateStoryRequestDTO dto, DataFetchingEnvironment env) {
    return generateStoryFacade.generateWebtoon(dto, env);
  }
}
