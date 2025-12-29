package com.example.genie_tune_java.domain.terms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_terms")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Terms {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Integer version;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(nullable = false)
  private boolean isRequired;

  @Column(nullable = false)
  @Builder.Default
  private LocalDateTime registeredAt = LocalDateTime.now();

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private TermsStatus termsStatus = TermsStatus.ACTIVE;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private TermsCategory termsCategory = TermsCategory.SERVICE;

}
