package com.example.genie_tune_java.domain.pay.entity;

import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_pay")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Pay {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @ManyToOne
  @JoinColumn(name="order_id", nullable = false)
  private Order order;

  @ManyToOne
  @JoinColumn(name="member_id", nullable = false)
  private Member member;

  @Column(nullable = false)
  private String paymentId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PayStatus payStatus;

  @Column(nullable = false)
  private Long amount;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String receiptUrl;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String failReason;

}
