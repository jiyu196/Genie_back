package com.example.genie_tune_java.domain.service_access.service;

import com.example.genie_tune_java.domain.service_access.dto.ServiceAccessRegisterInputDTO;

public interface ServiceAccessService {
  void issueServiceAccess(ServiceAccessRegisterInputDTO dto);
}
