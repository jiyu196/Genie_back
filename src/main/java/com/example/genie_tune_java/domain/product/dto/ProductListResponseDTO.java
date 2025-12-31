package com.example.genie_tune_java.domain.product.dto;

public record ProductListResponseDTO(
  int price, int duration, int maxPromptDailyCount, int maxWebToonStorage, String productStatus, String ProductGrade, String subscriptionCycle
) {}
