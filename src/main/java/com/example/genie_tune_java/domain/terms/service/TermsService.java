package com.example.genie_tune_java.domain.terms.service;

import com.example.genie_tune_java.domain.terms.dto.GetTermsRequestDTO;
import com.example.genie_tune_java.domain.terms.dto.GetTermsResponseDTO;

public interface TermsService {

  GetTermsResponseDTO getTerm(GetTermsRequestDTO dto);
}
