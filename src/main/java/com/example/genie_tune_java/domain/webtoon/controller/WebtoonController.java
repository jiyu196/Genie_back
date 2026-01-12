package com.example.genie_tune_java.domain.webtoon.controller;

import com.example.genie_tune_java.domain.webtoon.dto.page.WebtoonGroupResponseDTO;
import com.example.genie_tune_java.domain.webtoon.dto.page.WebtoonPageRequestDTO;
import com.example.genie_tune_java.domain.webtoon.dto.page.WebtoonPageResponseDTO;
import com.example.genie_tune_java.domain.webtoon.service.WebtoonService;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class WebtoonController {
  private final WebtoonService webtoonService;

  @QueryMapping
  public WebtoonPageResponseDTO getWebtoonPage(@Argument("input") WebtoonPageRequestDTO dto, DataFetchingEnvironment env) {

    List<WebtoonGroupResponseDTO> content =  webtoonService.getWebtoonGallery(env);
    return webtoonService.getWebtoonGalleryPage(dto, content);
  }
}
