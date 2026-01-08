package com.example.genie_tune_java.domain.service_access.service;

import com.example.genie_tune_java.domain.service_access.dto.ServiceAccessRegisterInputDTO;
import com.example.genie_tune_java.domain.service_access.dto.pageable.MyAccessIdPageRequestDTO;
import com.example.genie_tune_java.domain.service_access.dto.pageable.MyAccessIdPageResponseDTO;

public interface ServiceAccessService {
  void issueServiceAccess(ServiceAccessRegisterInputDTO dto);
  MyAccessIdPageResponseDTO getMyAccessIdPage(MyAccessIdPageRequestDTO dto);
}
