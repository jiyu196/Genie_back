package com.example.genie_tune_java.domain.subscription.service;

import com.example.genie_tune_java.domain.subscription.dto.SubscriptionRegisterInputDTO;
import com.example.genie_tune_java.domain.subscription.entity.Subscription;
import com.example.genie_tune_java.domain.subscription.mapper.SubscriptionMapper;
import com.example.genie_tune_java.domain.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class SubscriptionServiceImpl implements SubscriptionService {
  private final SubscriptionRepository subscriptionRepository;
  private final SubscriptionMapper subscriptionMapper;

  @Override
  @Transactional
  public Subscription registerSubscription(SubscriptionRegisterInputDTO dto) {
    Subscription subscription = subscriptionMapper.toSuccessSubscriptionEntity(dto);
    subscription.activate();
    subscriptionRepository.save(subscription);
    return subscription;
  }
}
