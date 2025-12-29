package com.example.genie_tune_java.domain.terms.mapper;

import com.example.genie_tune_java.domain.terms.dto.GetTermsResponseDTO;
import com.example.genie_tune_java.domain.terms.entity.Terms;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TermsMapper {

  //Entity에는 DTO 필드 값이 전부 담겨져 있음 그 중에서 DTO에 있는 값만 맵핑해줌
  GetTermsResponseDTO toDTO(Terms terms);
}
