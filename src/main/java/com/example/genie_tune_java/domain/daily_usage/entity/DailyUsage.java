package com.example.genie_tune_java.domain.daily_usage.entity;

import com.example.genie_tune_java.domain.service_access.entity.ServiceAccess;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_daily_usage")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyUsage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "service_access_id", nullable = false)
  private ServiceAccess serviceAccess;

  @Column
  @Builder.Default
  private LocalDate usageDate = LocalDate.now();

  @Column(nullable = false)
  @Builder.Default
  private int promptCount = 1;

  @Column(nullable = false)
  @Builder.Default
  private LocalDateTime usedAt = LocalDateTime.now();

}
