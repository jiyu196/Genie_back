package com.example.genie_tune_java.domain.pay.mapper;

import com.example.genie_tune_java.domain.pay.dto.payment_method.PayMethodRegisterInputDTO;
import com.example.genie_tune_java.domain.pay.entity.PayMethod;
import com.example.genie_tune_java.domain.pay.entity.PgType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PayMethodMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "member", source = "dto.member")
  @Mapping(target = "pgType", expression = "java(mapPgType(dto.pgType()))")
  @Mapping(target = "isDefault", ignore = true)
  @Mapping(target = "payMethodStatus", ignore = true)
  @Mapping(target = "registeredAt", ignore = true)
  @Mapping(target = "deletedAt", ignore = true)
  @Mapping(target = "billingKey", ignore = true)
  PayMethod toSuccessEntity(PayMethodRegisterInputDTO dto);

  default PgType mapPgType(String value) {
    if (value == null) {
      throw new IllegalArgumentException("pgType is null");
    }

    return switch (value) {
      case "PaymentMethodCard" -> PgType.CARD;
      case "BANK_TRANSFER" -> PgType.BANK_TRANSFER;
      default -> throw new IllegalArgumentException("Unknown pgType: " + value);
    };
  }
}
