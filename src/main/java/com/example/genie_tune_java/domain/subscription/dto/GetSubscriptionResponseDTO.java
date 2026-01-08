package com.example.genie_tune_java.domain.subscription.dto;

import java.time.LocalDateTime;

public record GetSubscriptionResponseDTO(
        String productName, // product의 displayName 외래키
        String productGrade, // product 외래키
        String subscriptionCycle, // product 외래키
        LocalDateTime startedAt, // subscription의 startDate
        LocalDateTime endedAt, // subscription의 endDate
        int issuedAccessCount, // product의 maxServiceAccessIdCount
        String status // subscription 의 SubscriptionStatus
) {}
