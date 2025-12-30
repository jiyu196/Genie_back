package com.example.genie_tune_java.domain.subscription.entity;

import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.order.entity.Order;
import com.example.genie_tune_java.domain.pay.entity.PayMethod;
import com.example.genie_tune_java.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_subscription")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  //Order 객체 들어갈 자리
  @OneToOne
  @JoinColumn(name= "order_id", nullable = false)
  private Order order;

  //Product 객체 들어갈 자리
  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  //PayMethod 객체 들어갈 자리
  @ManyToOne
  @JoinColumn(name = "pay_method_id", nullable = false)
  private PayMethod payMethod;

  @Column
  @Builder.Default
  private LocalDateTime startDate = LocalDateTime.now();

  @Column
  private LocalDateTime nextBillingDate;

  @Column
  private LocalDateTime endDate;

  @Column
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private SubscriptionStatus subscriptionStatus = SubscriptionStatus.PENDING;

  @Column
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public void activate(LocalDateTime startAt) {
    this.subscriptionStatus = SubscriptionStatus.ACTIVE;
    this.startDate = startAt;
    // 비즈니스 규칙: 구독은 한 달 단위
    this.nextBillingDate = startAt.plusMonths(1);
    this.endDate = startAt.plusMonths(1);
  }

  // 해지 시 상태만 변경하는 메서드
  public void cancel() {
    this.subscriptionStatus = SubscriptionStatus.CANCELED;
    // endDate는 유지 (남은 기간까지는 써야 하므로)
  }
}
