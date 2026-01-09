package com.example.genie_tune_java.domain.prompt.mapper;

import com.example.genie_tune_java.domain.prompt.dto.register.PreRegisterPromptDTO;
import com.example.genie_tune_java.domain.prompt.entity.Prompt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PromptMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "serviceAccess", source = "dto.serviceAccess")
  @Mapping(target = "revisedContent", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  Prompt toEntityForRegister(PreRegisterPromptDTO dto);
}
