package com.example.genie_tune_java.domain.webtoon.service;

import com.example.genie_tune_java.domain.webtoon.dto.WebtoonRegisterDTO;
import com.example.genie_tune_java.domain.webtoon.entity.Webtoon;

public interface WebtoonService {
  Webtoon register(WebtoonRegisterDTO dto);
}
