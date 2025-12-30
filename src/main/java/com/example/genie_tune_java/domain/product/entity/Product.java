package com.example.genie_tune_java.domain.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_product")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private int price;

  @Column(nullable = false)
  private int duration;

  @Column(nullable = false)
  private int maxPromptDailyCount;

  @Column(nullable = false)
  private int maxWebtoonStorage;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private ProductStatus productStatus = ProductStatus.ACTIVE;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private ProductGrade productGrade = ProductGrade.BASIC;

  @Column
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private SubscriptionCycle subscriptionCycle = SubscriptionCycle.MONTHLY;

  @Column
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();
}
