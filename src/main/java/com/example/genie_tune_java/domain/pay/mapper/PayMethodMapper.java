package com.example.genie_tune_java.domain.pay.mapper;

import com.example.genie_tune_java.domain.pay.dto.payment_method.PayMethodRegisterInputDTO;
import com.example.genie_tune_java.domain.pay.entity.PayMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PayMethodMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "member", source = "dto.member")
  @Mapping(target = "isDefault", ignore = true)
  @Mapping(target = "payMethodStatus", ignore = true)
  @Mapping(target = "registeredAt", ignore = true)
  @Mapping(target = "deletedAt", ignore = true)
  @Mapping(target = "billingKey", ignore = true)
  PayMethod toSuccessEntity(PayMethodRegisterInputDTO dto);
}
