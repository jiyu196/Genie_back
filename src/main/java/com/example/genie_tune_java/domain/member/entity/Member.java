package com.example.genie_tune_java.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Member {
  //PK
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  //가입시 입력정보
  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false, unique = true)
  private String bizNumber; // API 호출시 검증 필요
  @Column(nullable = false, unique = true)
  private String organizationName; //API 호출로 가져옴
  //default 값 매칭 or 자동 매칭
  @Column(nullable = false)
  private boolean isAdmin;
  @Column(nullable = false)
  private LocalDateTime registeredAt;
  @Column
  private LocalDateTime deletedAt;

  //상태값 관리(Enum)
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RegisterStatus status;
  
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AccountStatus accountStatus;

}
