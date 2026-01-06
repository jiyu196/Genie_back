package com.example.genie_tune_java.domain.service_access.mapper;

import com.example.genie_tune_java.domain.service_access.dto.ServiceAccessRegisterInputDTO;
import com.example.genie_tune_java.domain.service_access.entity.ServiceAccess;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceAccessMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "accessId", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "deletedAt", ignore = true)
  @Mapping(target = "accessStatus", ignore = true)
  @Mapping(target = "expiredAt", ignore = true)
  @Mapping(target = "accessHash", ignore = true)
  @Mapping(target = "encryptedKey", ignore = true)
  ServiceAccess toIssueServiceAccess(ServiceAccessRegisterInputDTO dto);
}
