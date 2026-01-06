package com.example.genie_tune_java.domain.order.mapper;

import com.example.genie_tune_java.domain.order.dto.MakeOrderResponseDTO;
import com.example.genie_tune_java.domain.order.dto.OrderInputDTO;
import com.example.genie_tune_java.domain.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  Order toEntity(OrderInputDTO dto);

  @Mapping(target = "organizationName", source = "order.member.organizationName")
  @Mapping(target = "productId", source = "order.product.id")
  @Mapping(target = "displayName", source = "order.product.displayName")
  @Mapping(target="storeId", ignore = true)
  @Mapping(target="channelKey", ignore = true)
  MakeOrderResponseDTO toMakeOrderResponseDTO(Order order);

}
