package com.example.genie_tune_java.domain.service_access.service;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.service_access.dto.ServiceAccessRegisterInputDTO;
import com.example.genie_tune_java.domain.service_access.entity.ServiceAccess;
import com.example.genie_tune_java.domain.service_access.mapper.ServiceAccessMapper;
import com.example.genie_tune_java.domain.service_access.repository.ServiceAccessRepository;
import com.example.genie_tune_java.security.util.AESUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class ServiceAccessServiceImpl implements ServiceAccessService {
  private final ServiceAccessRepository serviceAccessRepository;
  private final ServiceAccessMapper serviceAccessMapper;
  private final PasswordEncoder passwordEncoder;
  private final AESUtil aesUtil;

  private static final int PROMPT_UNIT = 30;

  @Override
  @Transactional
  public void issueServiceAccess(ServiceAccessRegisterInputDTO dto) {

    Integer maxPromptDailyCount = serviceAccessRepository.findMaxPromptDailyCount(dto.subscription().getId()).orElseThrow(() -> new GlobalException(ErrorCode.PRODUCT_MAX_PROMPT_DAILY_COUNT_NOT_FOUNT));
    List<ServiceAccess> serviceAccessList = new ArrayList<>();

    for(int i=0; i<maxPromptDailyCount/PROMPT_UNIT; i++) {
    ServiceAccess serviceAccess = serviceAccessMapper.toIssueServiceAccess(dto);
      String serviceAccessId = "SAID-" + UUID.randomUUID();

      String encryptedKey = aesUtil.encrypt(serviceAccessId);

      String accessHash = passwordEncoder.encode(encryptedKey);

      serviceAccess.inputAccessId(serviceAccessId, encryptedKey, accessHash);

      serviceAccessList.add(serviceAccess);
    }
      serviceAccessRepository.saveAll(serviceAccessList);
  }
}
