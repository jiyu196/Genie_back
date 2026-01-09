package com.example.genie_tune_java.domain.prompt.mapper;

import com.example.genie_tune_java.domain.prompt.dto.register.RegisterPromptLogDTO;
import com.example.genie_tune_java.domain.prompt.entity.PromptLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PromptLogMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "prompt", source = "dto.prompt")
  @Mapping(target = "createdAt", ignore = true)
  PromptLog toEntityForRegister(RegisterPromptLogDTO dto);
}
