package com.example.genie_tune_java.domain.service_access.repository;

import com.example.genie_tune_java.domain.service_access.entity.ServiceAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ServiceAccessRepository extends JpaRepository<ServiceAccess, Long> {

  @Query("SELECT p.maxPromptDailyCount from Subscription s " +
          "LEFT JOIN s.order o " +
          "LEFT JOIN o.product p " +
          "WHERE s.id = :subscriptionId")
  Optional<Integer> findMaxPromptDailyCount(@Param("subscriptionId") Long subscriptionId);
}
