package com.example.genie_tune_java.domain.register_request.entity;

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

  @Column(nullable = false)
  private String bizNumber;

  @Column(nullable = false, name = "b_stt")
  private String businessStatus; // 계속사업자 / 휴업자 / 폐업자

  @Column(nullable = false, name = "b_stt_cd")
  private String businessStatusCode; // 01 / 02 / 03

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private RegisterRequestStatus registerRequestStatus = RegisterRequestStatus.PENDING;

  @Column(nullable = false)
  @Builder.Default
  private LocalDateTime checkedAt = LocalDateTime.now();
}
