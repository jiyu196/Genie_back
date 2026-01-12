package com.example.genie_tune_java.domain.webtoon.entity;

import com.example.genie_tune_java.domain.prompt.entity.Prompt;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_webtoon")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Webtoon {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @Builder.Default
  private String title = "제목없음";

  @OneToOne
  @JoinColumn(name = "prompt_id", nullable = false)
  private Prompt prompt;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private WebtoonStatus webtoonStatus;

  @Column(nullable = false)
  private String webtoonGroupId;

  @Column(nullable = false)
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();
}
