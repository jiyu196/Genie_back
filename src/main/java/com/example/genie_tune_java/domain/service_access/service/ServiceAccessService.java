package com.example.genie_tune_java.domain.service_access.service;

import com.example.genie_tune_java.domain.service_access.dto.ServiceAccessRegisterInputDTO;
import com.example.genie_tune_java.domain.service_access.dto.pageable.MyAccessIdPageRequestDTO;
import com.example.genie_tune_java.domain.service_access.dto.pageable.MyAccessIdPageResponseDTO;
import com.example.genie_tune_java.domain.service_access.entity.ServiceAccess;
import graphql.schema.DataFetchingEnvironment;

public interface ServiceAccessService {
  void issueServiceAccess(ServiceAccessRegisterInputDTO dto);
  MyAccessIdPageResponseDTO getMyAccessIdPage(MyAccessIdPageRequestDTO dto);
  ServiceAccess getServiceAccessInEnv(DataFetchingEnvironment env);
  ServiceAccess getServiceAccessFromKey(String decryptedKey);
}
