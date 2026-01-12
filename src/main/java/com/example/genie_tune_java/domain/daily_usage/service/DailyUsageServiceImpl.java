package com.example.genie_tune_java.domain.daily_usage.service;

import com.example.genie_tune_java.common.dto.ServiceAccessIdPrincipal;
import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.daily_usage.dto.DailyUsageResponseDTO;
import com.example.genie_tune_java.domain.daily_usage.entity.DailyUsage;
import com.example.genie_tune_java.domain.daily_usage.mapper.DailyUsageMapper;
import com.example.genie_tune_java.domain.daily_usage.repository.DailyUsageRepository;
import com.example.genie_tune_java.domain.service_access.entity.ServiceAccess;
import com.example.genie_tune_java.domain.service_access.repository.ServiceAccessRepository;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class DailyUsageServiceImpl implements DailyUsageService {

  private final DailyUsageRepository dailyUsageRepository;
  private final DailyUsageMapper dailyUsageMapper;
  private final ServiceAccessRepository serviceAccessRepository;

  @Transactional
  public DailyUsageResponseDTO recordDailyUsage(DataFetchingEnvironment env) {

    ServiceAccessIdPrincipal principal = env.getGraphQlContext().get(ServiceAccessIdPrincipal.class);
    String accessId = principal.getAccessId();
    ServiceAccess serviceAccess = serviceAccessRepository.findByAccessId(accessId).orElseThrow(() -> new GlobalException(ErrorCode.SERVICE_ACCESS_NOTFOUND));

    //upsert 수행 없으면 insert 있으면 update 전부 repository(db)에 위임
    dailyUsageRepository.upsertAndIncrease(serviceAccess.getId());

    DailyUsage dailyUsage = dailyUsageRepository.findByServiceAccess(serviceAccess).orElseThrow(() -> new GlobalException(ErrorCode.DAILY_USAGE_NOT_FOUND));

    return dailyUsageMapper.toDTO(dailyUsage);
  }

  @Transactional
  public boolean checkDailyUsage(DataFetchingEnvironment env) {
    ServiceAccessIdPrincipal principal = env.getGraphQlContext().get(ServiceAccessIdPrincipal.class);
    String accessId = principal.getAccessId();
    ServiceAccess serviceAccess = serviceAccessRepository.findByAccessId(accessId).orElseThrow(() -> new GlobalException(ErrorCode.SERVICE_ACCESS_NOTFOUND));

    Optional<DailyUsage> optionalDailyUsage = dailyUsageRepository.findByServiceAccessAndUsageDate(serviceAccess, LocalDate.now());

    if(optionalDailyUsage.isEmpty()) {
      return true;
    }
    DailyUsage dailyUsage = optionalDailyUsage.get();

    return dailyUsage.getPromptCount() < 20;
  }

}
