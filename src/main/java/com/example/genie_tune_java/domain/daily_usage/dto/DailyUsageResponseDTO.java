package com.example.genie_tune_java.domain.daily_usage.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DailyUsageResponseDTO(
  LocalDate usageDate, //사용일자
  int promptCount // 사용한 량
) {}
