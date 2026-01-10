package com.example.genie_tune_java.domain.webtoon.service;

import com.example.genie_tune_java.domain.webtoon.dto.WebtoonRegisterDTO;
import com.example.genie_tune_java.domain.webtoon.entity.Webtoon;
import com.example.genie_tune_java.domain.webtoon.mapper.WebtoonMapper;
import com.example.genie_tune_java.domain.webtoon.repository.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class WebtoonServiceImpl implements WebtoonService {

  private final WebtoonRepository webtoonRepository;
  private final WebtoonMapper webtoonMapper;

  @Override
  @Transactional
  public Webtoon register(WebtoonRegisterDTO dto) {
    Webtoon webtoon = webtoonMapper.toEntityForRegister(dto);
    return webtoonRepository.save(webtoon);
  }
}
