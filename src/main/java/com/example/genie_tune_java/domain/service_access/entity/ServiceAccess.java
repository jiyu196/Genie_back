package com.example.genie_tune_java.domain.service_access.entity;

import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.subscription.entity.Subscription;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "tbl_service_access")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceAccess {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String accessId;

  @Column(nullable = false)
  private String accessHash;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String encryptedKey;

  @ManyToOne
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @ManyToOne
  @JoinColumn(name = "subscription_id", nullable = false)
  private Subscription subscription;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private AccessStatus accessStatus = AccessStatus.ACTIVE;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column
  private LocalDateTime deletedAt;

  @Column(nullable = false)
  private LocalDateTime expiredAt;

  public void inputAccessId(String accessId, String accessHash, String encryptedKey) {
    this.accessId = accessId;
    this.accessHash = accessHash;
    this.encryptedKey = encryptedKey;
  }

  public void applySubscriptionPeriod(LocalDateTime createdAt, LocalDateTime expiredAt) {
    this.createdAt = createdAt;
    this.expiredAt = expiredAt;
  }
}
