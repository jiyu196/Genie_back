package com.example.genie_tune_java.domain.terms.entity;

import com.example.genie_tune_java.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_member_terms")
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class MemberTerms {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="member_id", nullable = false)
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="terms_id", nullable = false)
  private Terms terms;

  @Column(nullable = false)
  @Builder.Default
  private LocalDateTime agreedAt =  LocalDateTime.now();

}
