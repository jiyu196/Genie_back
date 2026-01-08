package com.example.genie_tune_java.domain.service_access.controller;

import com.example.genie_tune_java.domain.service_access.dto.pageable.MyAccessIdPageRequestDTO;
import com.example.genie_tune_java.domain.service_access.dto.pageable.MyAccessIdPageResponseDTO;
import com.example.genie_tune_java.domain.service_access.service.ServiceAccessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ServiceAccessController {

  private final ServiceAccessService serviceAccessService;

  @QueryMapping
  public MyAccessIdPageResponseDTO getMyAccessIdPage(@Argument("input") MyAccessIdPageRequestDTO dto) {
    return serviceAccessService.getMyAccessIdPage(dto);
  }

}
