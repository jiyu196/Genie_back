package com.example.genie_tune_java.domain.admin.mapper;

import com.example.genie_tune_java.domain.admin.dto.manage_member.RegisterRequestResponseDTO;
import com.example.genie_tune_java.domain.admin.entity.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterRequestMapper {

  @Mapping(source = "member.email", target = "email")
  @Mapping(source = "member.organizationName", target = "organizationName")
  @Mapping(source = "member.bizNumber", target = "bizNumber")
  @Mapping(source = "member.representativeName", target = "representativeName")
  @Mapping(source = "member.contactName", target = "contactName")
  @Mapping(source = "member.role", target = "role")
  @Mapping(source = "member.accountStatus", target = "accountStatus")
  @Mapping(source = "member.approvedAt", target = "approvedAt")
  RegisterRequestResponseDTO toDto(RegisterRequest registerRequest);

}
