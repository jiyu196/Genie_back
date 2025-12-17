package com.example.genie_tune_java.domain.register_request.service;

import com.example.genie_tune_java.api.nts.NTSBusinessAPIClient;
import com.example.genie_tune_java.api.nts.service.BusinessStatusCheckService;
import com.example.genie_tune_java.domain.register_request.dto.RegisterRequestCheckDTO;
import com.example.genie_tune_java.domain.register_request.dto.RegisterRequestDTO;
import com.example.genie_tune_java.domain.register_request.dto.RegisterRequestResponseDTO;
import com.example.genie_tune_java.domain.register_request.entity.RegisterRequest;
import com.example.genie_tune_java.domain.register_request.mapper.RegisterRequestMapper;
import com.example.genie_tune_java.domain.register_request.repository.RegisterRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class RegisterRequestServiceImpl implements RegisterRequestService {
  private final BusinessStatusCheckService businessStatusCheckService;
  private final RegisterRequestRepository registerRequestRepository;
  private final RegisterRequestMapper registerRequestMapper;

  public RegisterRequestResponseDTO createRequest(RegisterRequestCheckDTO rrcDto) {
    log.info("rrcDto={}", rrcDto.bizNumber());
    RegisterRequestDTO rrDto = businessStatusCheckService.checkBusinessStatus(rrcDto);
    log.info("bizNumber={} registerRequestStatus= {}", rrDto.bizNumber(), rrDto.registerRequestStatus());
    log.info("{} {}", rrDto.businessStatus(), rrDto.businessStatusCode());
    RegisterRequest registerRequest = registerRequestMapper.toEntity(rrDto);
    log.info(registerRequest.getBusinessStatus());
    log.info(registerRequest.getBusinessStatusCode());
    registerRequestRepository.save(registerRequest);
    RegisterRequestResponseDTO registerRequestResponseDTO = registerRequestMapper.toDto(registerRequest);
    log.info("bStt={}, bSttCd = {}", registerRequestResponseDTO.businessStatus(), registerRequestResponseDTO.businessStatusCode());
    return registerRequestResponseDTO;
  }

}
