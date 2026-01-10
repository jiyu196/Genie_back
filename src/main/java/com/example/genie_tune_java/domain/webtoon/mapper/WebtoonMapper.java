package com.example.genie_tune_java.domain.webtoon.mapper;

import com.example.genie_tune_java.domain.webtoon.dto.WebtoonRegisterDTO;
import com.example.genie_tune_java.domain.webtoon.entity.Webtoon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WebtoonMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  Webtoon toEntityForRegister(WebtoonRegisterDTO dto);
}
