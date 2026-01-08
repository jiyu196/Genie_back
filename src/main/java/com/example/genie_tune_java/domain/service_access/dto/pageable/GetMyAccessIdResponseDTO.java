package com.example.genie_tune_java.domain.service_access.dto.pageable;

import com.example.genie_tune_java.domain.service_access.entity.AccessStatus;

import java.time.LocalDateTime;

public record GetMyAccessIdResponseDTO(
  String decryptedKey, // AES로 encrypt 한걸 decrypt 한걸로 내보냄
  AccessStatus accessStatus, // 서비스 ID 상태
  LocalDateTime createdAt, // 서비스 ID 생성일
  LocalDateTime expiredAt
) {}
