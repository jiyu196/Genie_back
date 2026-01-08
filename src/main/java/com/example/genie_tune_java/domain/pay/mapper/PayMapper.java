package com.example.genie_tune_java.domain.pay.mapper;

import com.example.genie_tune_java.domain.pay.dto.pageable.GetPaymentResponseDTO;
import com.example.genie_tune_java.domain.pay.dto.success.PaySuccessRegisterInputDTO;
import com.example.genie_tune_java.domain.pay.dto.success.PaySuccessRegisterOutputDTO;
import com.example.genie_tune_java.domain.pay.entity.Pay;
import com.example.genie_tune_java.domain.pay.entity.PayMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PayMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "transactionId", source = "dto.transactionId")
  @Mapping(target = "updatedAt", expression = "java(dto.paidAt().toLocalDateTime())")
  @Mapping(target = "receiptUrl", source = "dto.receiptUrl")
  @Mapping(target = "reason", ignore = true)
  Pay toSuccessEntity(PaySuccessRegisterInputDTO dto);

  @Mapping(target = "paidAt", source = "pay.updatedAt")
  PaySuccessRegisterOutputDTO toSuccessDto(Pay pay);

  @Mapping(target = "orderUuid", source = "pay.order.orderUuid")
  @Mapping(target = "paidAt", source = "pay.updatedAt")
  @Mapping(target = "cardCompany", source = "payMethod.cardCompany")
  @Mapping(target = "cardNumberMask", source = "payMethod.cardNumberMask")
  GetPaymentResponseDTO toGetPaymentDto(Pay pay, PayMethod payMethod);
}
