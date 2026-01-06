package com.example.genie_tune_java.domain.service_access.service;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.service_access.dto.ServiceAccessRegisterInputDTO;
import com.example.genie_tune_java.domain.service_access.entity.ServiceAccess;
import com.example.genie_tune_java.domain.service_access.mapper.ServiceAccessMapper;
import com.example.genie_tune_java.domain.service_access.repository.ServiceAccessRepository;
import com.example.genie_tune_java.security.util.AESUtil;
import com.example.genie_tune_java.security.util.CookieUtil;
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



  @Override
  @Transactional
  public void issueServiceAccess(ServiceAccessRegisterInputDTO dto) {

    Integer maxServiceAccessIdCount = serviceAccessRepository.findMaxServiceAccessIdCount(dto.subscription().getId()).orElseThrow(() -> new GlobalException(ErrorCode.PRODUCT_MAX_PROMPT_DAILY_COUNT_NOT_FOUNT));
    List<ServiceAccess> serviceAccessList = new ArrayList<>();

    for(int i=0; i<maxServiceAccessIdCount; i++) {
    ServiceAccess serviceAccess = serviceAccessMapper.toIssueServiceAccess(dto);
      String serviceAccessIdOriginal = "SAID-" + UUID.randomUUID();

      String encryptedKey = aesUtil.encrypt(serviceAccessIdOriginal);

      String accessHash = passwordEncoder.encode(serviceAccessIdOriginal);

      String accessId = serviceAccessIdOriginal.substring(0, 12);

      serviceAccess.inputAccessId(accessId, encryptedKey, accessHash);
      serviceAccess.applySubscriptionPeriod(dto.subscription().getStartDate(), dto.subscription().getEndDate());

      serviceAccessList.add(serviceAccess);
    }
      serviceAccessRepository.saveAll(serviceAccessList);
  }
}
