package com.example.genie_tune_java.domain.terms.service;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.terms.dto.GetTermsRequestDTO;
import com.example.genie_tune_java.domain.terms.dto.GetTermsResponseDTO;
import com.example.genie_tune_java.domain.terms.entity.Terms;
import com.example.genie_tune_java.domain.terms.entity.TermsCategory;
import com.example.genie_tune_java.domain.terms.mapper.TermsMapper;
import com.example.genie_tune_java.domain.terms.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TermsServiceImpl implements TermsService {
  private final TermsRepository termsRepository;
  private final TermsMapper termsMapper;

  @Override
  public GetTermsResponseDTO getTerm(GetTermsRequestDTO dto) {
    Terms term = termsRepository.findLatestByCategory(Enum.valueOf(TermsCategory.class, dto.termsCategory())).orElseThrow(() -> new GlobalException(ErrorCode.TERMS_NOT_FOUND));

    return termsMapper.toDTO(term);
  }
}
