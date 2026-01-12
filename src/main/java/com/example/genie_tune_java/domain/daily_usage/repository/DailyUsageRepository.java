package com.example.genie_tune_java.domain.daily_usage.repository;

import com.example.genie_tune_java.domain.daily_usage.entity.DailyUsage;
import com.example.genie_tune_java.domain.service_access.entity.ServiceAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyUsageRepository extends JpaRepository<DailyUsage, Long> {

  @Modifying
  @Query(value = """
  INSERT INTO tbl_daily_usage (service_access_id, usage_date, used_at)
  VALUES (:serviceAccessId, CURRENT_DATE, NOW())
  ON CONFLICT (service_access_id, usage_date)
  DO UPDATE SET
    prompt_count = tbl_daily_usage.prompt_count + 1, used_at = NOW()
""", nativeQuery = true)
  void upsertAndIncrease(@Param("serviceAccessId") Long serviceAccessId);

  Optional<DailyUsage> findByServiceAccess(ServiceAccess serviceAccess);

  Optional<DailyUsage> findByServiceAccessAndUsageDate(ServiceAccess serviceAccess, LocalDate usageDate);
}
