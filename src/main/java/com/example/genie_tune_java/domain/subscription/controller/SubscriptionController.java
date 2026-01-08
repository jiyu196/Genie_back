package com.example.genie_tune_java.domain.subscription.controller;

import com.example.genie_tune_java.domain.subscription.dto.GetSubscriptionResponseDTO;
import com.example.genie_tune_java.domain.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Log4j2
public class SubscriptionController {
  private final SubscriptionService subscriptionService;

  @QueryMapping
  public GetSubscriptionResponseDTO getMySubscription() {
    return subscriptionService.getSubscription();
  }
}
