package com.example.genie_tune_java.domain.prompt.entity;

import com.example.genie_tune_java.domain.service_access.entity.ServiceAccess;
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

  @ManyToOne
  @JoinColumn(name = "service_access_id", nullable = false)
  private ServiceAccess serviceAccess;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String originalContent;

  @Column(columnDefinition = "TEXT")
  private String filteredContent;

  @Column(columnDefinition = "TEXT")
  private String refinedContent;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String revisedContent;

  @Column
  private boolean isSlang;

  @Column
  @Enumerated(EnumType.STRING)
  @Builder.Default
  PromptStatus promptStatus = PromptStatus.WAITING;

  @Column
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();
}
