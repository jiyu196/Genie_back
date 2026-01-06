package com.example.genie_tune_java.common.dto;

import com.example.genie_tune_java.domain.service_access.entity.AccessStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class ServiceAccessIdPrincipal {
  private final Long memberId;
  private final AccessStatus accessStatus;
  private final LocalDateTime expiredAt;
}
