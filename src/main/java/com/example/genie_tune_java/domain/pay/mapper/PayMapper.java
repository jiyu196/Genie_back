package com.example.genie_tune_java.domain.pay.mapper;

import com.example.genie_tune_java.domain.pay.dto.success.PaySuccessRegisterInputDTO;
import com.example.genie_tune_java.domain.pay.dto.success.PaySuccessRegisterOutputDTO;
import com.example.genie_tune_java.domain.pay.entity.Pay;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PayMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "transactionId", source = "dto.paidPayment.transactionId")
  @Mapping(target = "updatedAt", source = "dto.paidPayment.paidAt")
  @Mapping(target = "receiptUrl", source = "dto.paidPayment.receiptUrl")
  @Mapping(target = "reason", ignore = true)
  Pay toSuccessEntity(PaySuccessRegisterInputDTO dto);

  PaySuccessRegisterOutputDTO toSuccessDto(Pay pay);
}
