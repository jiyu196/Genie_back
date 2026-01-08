package com.example.genie_tune_java.domain.subscription.repository;

import com.example.genie_tune_java.domain.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
  @Query("SELECT s FROM Subscription s " +
          "JOIN FETCH s.product p " + // Product 정보를 즉시 로딩
          "WHERE s.member.id = :memberId " +
          "AND s.subscriptionStatus = 'ACTIVE' " + // 현재 활성화된 구독만
          "ORDER BY s.startDate DESC") // 만약 여러개라면 가장 최근 것)
  Optional<Subscription> findActiveSubscriptionWithProduct(@Param("memberId") Long memberId);
}
