package com.example.genie_tune_java.domain.webtoon.controller;

import com.example.genie_tune_java.domain.service_access.entity.ServiceAccess;
import com.example.genie_tune_java.domain.service_access.service.ServiceAccessService;
import com.example.genie_tune_java.domain.webtoon.dto.page.MyPageWebtoonRequestDTO;
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
  private final ServiceAccessService serviceAccessService;

  @QueryMapping
  public WebtoonPageResponseDTO getWebtoonPage(@Argument("input") WebtoonPageRequestDTO dto, DataFetchingEnvironment env) {
    List<WebtoonGroupResponseDTO> content =  webtoonService.getWebtoonGallery(serviceAccessService.getServiceAccessInEnv(env));
    return webtoonService.getWebtoonGalleryPage(dto.page(), dto.size(), content);
  }

  @QueryMapping
  public WebtoonPageResponseDTO getWebtoonForMyPage(@Argument("input") MyPageWebtoonRequestDTO dto) {
    List<WebtoonGroupResponseDTO> content =  webtoonService.getWebtoonGallery(serviceAccessService.getServiceAccessFromKey(dto.decryptedKey()));
    return webtoonService.getWebtoonGalleryPage(dto.page(), dto.size(), content);
  }
}
