package com.example.genie_tune_java.domain.daily_usage.service;

import com.example.genie_tune_java.domain.daily_usage.dto.DailyUsageResponseDTO;
import graphql.schema.DataFetchingEnvironment;

public interface DailyUsageService {
  public boolean checkDailyUsage(DataFetchingEnvironment env);
  public DailyUsageResponseDTO recordDailyUsage(DataFetchingEnvironment env);
}
