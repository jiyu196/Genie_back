package com.example.genie_tune_java.domain.terms.controller;

import com.example.genie_tune_java.domain.terms.dto.GetTermsRequestDTO;
import com.example.genie_tune_java.domain.terms.dto.GetTermsResponseDTO;
import com.example.genie_tune_java.domain.terms.service.TermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TermsController {
  private final TermsService termsService;

  @QueryMapping
  public GetTermsResponseDTO getTerm(@Argument("input") GetTermsRequestDTO dto) {
    return termsService.getTerm(dto);
  }
}
