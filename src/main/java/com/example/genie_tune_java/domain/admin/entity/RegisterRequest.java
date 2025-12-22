package com.example.genie_tune_java.domain.admin.entity;

import com.example.genie_tune_java.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_register_request")
@Builder
public class RegisterRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="member_id", nullable=false, unique = true)
  private Member member;

  @Column
  private String rejectReason;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private RegisterStatus registerStatus = RegisterStatus.PENDING;

  @Column(nullable = false)
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column
  private LocalDateTime checkedAt;

  public static RegisterRequest createRequest(Member member) {

    return RegisterRequest.builder()
            .member(member)
            .registerStatus(RegisterStatus.PENDING)
            .createdAt(LocalDateTime.now())
            .build();
  }
}
