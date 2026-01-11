package com.example.genie_tune_java.domain.attach.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_attach")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attach {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(nullable = false)
  Long targetId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  AttachTargetType attachTargetType;

  @Column(nullable = false, name = "s3_key")
  String s3Key;

  @Column(nullable = false)
  String fileName;

  @Column
  String originName;

  @Column
  String extension;

  @Column
  String contentType;

  @Column
  Long fileSize;

  @Column(nullable = false)
  @Builder.Default
  LocalDateTime createdAt = LocalDateTime.now();
}
