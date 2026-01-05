package com.example.genie_tune_java.domain.pay.entity;

import com.example.genie_tune_java.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_pay_method")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayMethod {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PgType pgType;

  @Column(nullable = false)
  private String cardCompany;

  @Column(nullable = false)
  private String cardNumberMask;

  @Column(nullable = false)
  @Builder.Default
  private boolean isDefault = true;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private PayMethodStatus payMethodStatus = PayMethodStatus.ACTIVE;

  @Column
  private String billingKey;

  @Column(nullable = false)
  @Builder.Default
  private LocalDateTime registeredAt = LocalDateTime.now();

  @Column
  private LocalDateTime deletedAt;
}
