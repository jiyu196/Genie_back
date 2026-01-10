package com.example.genie_tune_java.domain.attach.mapper;

import com.example.genie_tune_java.domain.attach.dto.AttachRegisterDTO;
import com.example.genie_tune_java.domain.attach.dto.AttachResponseDTO;
import com.example.genie_tune_java.domain.attach.entity.Attach;
import io.lettuce.core.dynamic.annotation.Param;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttachMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  Attach toAttachForRegister(AttachRegisterDTO dto);

  AttachResponseDTO toAttachResponseDTO(Attach attach, String fileUrl);
}
