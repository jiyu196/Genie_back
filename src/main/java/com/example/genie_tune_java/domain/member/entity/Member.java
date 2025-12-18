package com.example.genie_tune_java.domain.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "tbl_member")
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
  @Column(nullable = false)
  String representativeName;
  @Column(nullable = false)
  String openingDate;
  @Column(nullable = false)
  private String contactName;

  //DB가 자동
  @Column(nullable = false)
  @Builder.Default
  private LocalDateTime registeredAt = LocalDateTime.now();
  // 삭제시에만 기록
  @Column
  private LocalDateTime deletedAt;
  //승인 시점
  @Column
  private LocalDateTime approvedAt;

  //저장 상태
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private RegisterStatus registerStatus = RegisterStatus.PENDING;
  //default 값 MEMBER 매칭
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private Role role = Role.MEMBER;

  //계정상태
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private AccountStatus accountStatus = AccountStatus.ACTIVE;


  //password 저장 메서드
  public void savePassword(String encodedPassword) {
    this.password = encodedPassword;
  }
}
