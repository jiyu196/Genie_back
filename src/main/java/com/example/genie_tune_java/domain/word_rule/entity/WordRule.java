package com.example.genie_tune_java.domain.word_rule.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_word_rule")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WordRule {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String forbiddenWord;

  @Column
  private String cleanWord;

  @Column
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column
  private LocalDateTime deletedAt;

  @Column
  @Enumerated(EnumType.STRING)
  private WordRuleStatus status;

  @Column(columnDefinition = "TEXT")
  private String reason;

}
