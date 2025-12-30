package com.example.genie_tune_java.domain.subscription.repository;

import com.example.genie_tune_java.domain.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
