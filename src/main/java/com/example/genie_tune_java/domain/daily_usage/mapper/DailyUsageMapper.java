package com.example.genie_tune_java.domain.daily_usage.mapper;

import com.example.genie_tune_java.domain.daily_usage.dto.DailyUsageResponseDTO;
import com.example.genie_tune_java.domain.daily_usage.entity.DailyUsage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DailyUsageMapper {

  DailyUsageResponseDTO toDTO(DailyUsage entity);
}
