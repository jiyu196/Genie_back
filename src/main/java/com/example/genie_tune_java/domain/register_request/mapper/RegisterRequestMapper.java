package com.example.genie_tune_java.domain.register_request.mapper;

import com.example.genie_tune_java.domain.register_request.dto.RegisterRequestDTO;
import com.example.genie_tune_java.domain.register_request.dto.RegisterRequestResponseDTO;
import com.example.genie_tune_java.domain.register_request.entity.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterRequestMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "checkedAt", ignore = true)
  @Mapping(target = "registerRequestStatus", ignore = true)
  RegisterRequest toEntity(RegisterRequestDTO dto);

  //lombok의 getter가 bStt를 getBStt로 인식하는 이슈가 있는 듯
  RegisterRequestResponseDTO toDto(RegisterRequest entity);
}
