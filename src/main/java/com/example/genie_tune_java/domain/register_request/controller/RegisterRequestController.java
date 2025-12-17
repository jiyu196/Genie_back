package com.example.genie_tune_java.domain.register_request.controller;

import com.example.genie_tune_java.domain.register_request.dto.RegisterRequestCheckDTO;
import com.example.genie_tune_java.domain.register_request.dto.RegisterRequestResponseDTO;
import com.example.genie_tune_java.domain.register_request.service.RegisterRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Log4j2
@RequiredArgsConstructor
public class RegisterRequestController {
  private final RegisterRequestService registerRequestService;

  @MutationMapping
  public RegisterRequestResponseDTO createRequest(@Argument("input") RegisterRequestCheckDTO dto) {

    return registerRequestService.createRequest(dto);
  }
}
