package com.example.genie_tune_java.domain.register_request.service;

import com.example.genie_tune_java.domain.register_request.dto.RegisterRequestCheckDTO;
import com.example.genie_tune_java.domain.register_request.dto.RegisterRequestResponseDTO;

public interface RegisterRequestService {
  RegisterRequestResponseDTO createRequest(RegisterRequestCheckDTO dto);
}
