package com.example.genie_tune_java.domain.subscription.service;

import com.example.genie_tune_java.domain.subscription.dto.SubscriptionRegisterInputDTO;
import com.example.genie_tune_java.domain.subscription.entity.Subscription;

public interface SubscriptionService {
  Subscription registerSubscription(SubscriptionRegisterInputDTO dto);
}
