package com.example.genie_tune_java.domain.order.entity;

import com.example.genie_tune_java.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_order")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String orderUuid;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private OrderStatus orderStatus = OrderStatus.PENDING;

  @Column(nullable = false)
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(nullable = false)
  @Builder.Default
  private Long totalAmount = 0L;

  public void changeOrderStatus(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }
}
