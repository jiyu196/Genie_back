package com.example.genie_tune_java.domain.member.mapper;

import com.example.genie_tune_java.domain.member.dto.*;
import com.example.genie_tune_java.domain.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {
  // target을 잡고, 자동생성 해주는 친구들 or 별도 주입은 ignore로 설정
  @Mapping(target= "id", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "registeredAt", ignore = true)
  @Mapping(target = "deletedAt", ignore = true)
  @Mapping(target = "approvedAt", ignore = true)
  @Mapping(target = "registerStatus", ignore = true)
  @Mapping(target = "role", ignore = true)
  @Mapping(target = "accountStatus", ignore = true)
  Member registerMember(MemberRegisterRequestDTO dto);

  //DTO에 있는 필드가 Entity에 전부 있으므로 Mapping을 붙이지 않아도 된다.
  MemberRegisterResponseDTO toRegisterResponseDTO(Member member);

  MemberGetResponseDTO toMemberGetResponseDTO(Member member);
}
