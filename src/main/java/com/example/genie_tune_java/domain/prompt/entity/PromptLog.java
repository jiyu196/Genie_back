package com.example.genie_tune_java.domain.prompt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_prompt_log")
@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class PromptLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // ===== FK =====
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Prompt prompt;

  // ===== 탐지 위치 =====
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SourceType sourceType;

  // ===== 단어 정보 =====
  @Column(nullable = false)
  private String originalWord;

  @Column
  private String filteredWord;

  // ===== 사유 =====
  @Column(columnDefinition = "text")
  private String reason;

  // ===== 에러 메시지 =====
  @Column(columnDefinition = "text")
  private String errorMessage;

  // ===== 생성 시각 =====
  @Column
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();
}
