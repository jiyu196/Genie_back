package com.example.genie_tune_java.domain.webtoon.service;

import com.example.genie_tune_java.domain.service_access.entity.ServiceAccess;
import com.example.genie_tune_java.domain.webtoon.dto.page.WebtoonGroupResponseDTO;
import com.example.genie_tune_java.domain.webtoon.dto.WebtoonRegisterDTO;
import com.example.genie_tune_java.domain.webtoon.dto.page.WebtoonPageRequestDTO;
import com.example.genie_tune_java.domain.webtoon.dto.page.WebtoonPageResponseDTO;
import com.example.genie_tune_java.domain.webtoon.entity.Webtoon;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

public interface WebtoonService {
  Webtoon register(WebtoonRegisterDTO dto);
  List<WebtoonGroupResponseDTO> getWebtoonGallery(ServiceAccess serviceAccess);
  WebtoonPageResponseDTO getWebtoonGalleryPage(int page, int size, List<WebtoonGroupResponseDTO> content, Long serviceAccessId);
}
