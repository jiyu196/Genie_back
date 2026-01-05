package com.example.genie_tune_java.domain.subscription.mapper;

import com.example.genie_tune_java.domain.subscription.dto.SubscriptionRegisterInputDTO;
import com.example.genie_tune_java.domain.subscription.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "startDate", ignore = true)
  @Mapping(target = "nextBillingDate", ignore = true)
  @Mapping(target = "endDate", ignore = true)
  @Mapping(target = "subscriptionStatus", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Subscription toSuccessSubscriptionEntity(SubscriptionRegisterInputDTO dto);
}
