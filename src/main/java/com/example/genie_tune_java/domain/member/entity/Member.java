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

  //DB가 자동
  @Column(nullable = false)
  @Builder.Default
  private LocalDateTime registeredAt = LocalDateTime.now();
  // 삭제시에만 기록
  @Column
  private LocalDateTime deletedAt;

  //저장 상태
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private RegisterStatus registerStatus = RegisterStatus.PENDING;
  //계정상태
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private AccountStatus accountStatus = AccountStatus.ACTIVE;

  //default 값 MEMBER 매칭
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private Role role = Role.MEMBER;

  //password 저장 메서드
  public void savePassword(String encodedPassword) {
    this.password = encodedPassword;
  }
}
