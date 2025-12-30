package com.example.genie_tune_java.domain.prompt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_prompt")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Prompt {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //Service Access 객체 들어갈 자리

  @Column(nullable = false, columnDefinition = "TEXT")
  private String originalContent;

  @Column
  private String filteredContent;

  @Column
  private String refinedContent;

  @Column
  @Enumerated(EnumType.STRING)
  PromptStatus promptStatus;

  @Column
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();
}
