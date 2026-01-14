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

  //가입 저장 요청 시점
  @Column(nullable = false)
  @Builder.Default
  private LocalDateTime registeredAt = LocalDateTime.now();
  // 삭제시에만 기록
  @Column
  private LocalDateTime deletedAt;
  //승인 시점
  @Column
  private LocalDateTime approvedAt;

  //임시비밀번호 발급 여부
  @Column
  @Builder.Default
  private boolean isTempPassword = false;

  //비밀번호 변경 시점
  @Column
  private LocalDateTime passwordUpdatedAt;

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

  //password 저장 메서드 update 된 시점도 기록
  public void updatePassword(String encodedPassword, LocalDateTime passwordUpdatedAt) {
    this.password = encodedPassword;
    this.passwordUpdatedAt = passwordUpdatedAt;
  }

  //가입상태 값 변경 메서드
  public void changeRegisterStatus(RegisterStatus newStatus) {
    this.registerStatus = newStatus;
  }
  //Mypage 내 대표자명, 담당자명 변경 엔티티 메서드
  public void changeInfo(String representativeName, String contactName) {
    this.representativeName = representativeName;
    this.contactName = contactName;
  }
  //임시비밀번호 발급시 임시비밀번호 여부 T/F로 boolean 값 저장
  public void checkIsTempPassword(boolean isTempPassword) {
    this.isTempPassword = isTempPassword;
  }

  //회원탈퇴 시, 멤버 상태 값 변경 엔티티 메서드
  public void softDelete() {
    this.accountStatus = AccountStatus.DELETED;
    this.deletedAt = LocalDateTime.now();
  }
  //회원 복구 메서드, 관리자 페이지에서 관리
  public void restore() {
    this.accountStatus = AccountStatus.ACTIVE;
    this.deletedAt = null;
  }

  //Member 권한 설정
  public void changeMemberRole(Role role) {
    this.role = role;
  }
}
