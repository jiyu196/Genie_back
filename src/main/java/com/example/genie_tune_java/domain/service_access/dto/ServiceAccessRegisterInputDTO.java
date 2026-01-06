package com.example.genie_tune_java.domain.service_access.dto;

import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.subscription.entity.Subscription;

public record ServiceAccessRegisterInputDTO(
  Member member,
  Subscription subscription
) {}
