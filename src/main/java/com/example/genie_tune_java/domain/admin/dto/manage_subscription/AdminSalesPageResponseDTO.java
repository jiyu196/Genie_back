package com.example.genie_tune_java.domain.admin.dto.manage_subscription;

import com.example.genie_tune_java.domain.admin.dto.manage_member.RegisterRequestResponseDTO;

import java.util.List;

public record AdminSalesPageResponseDTO(
  List<PayInfoResponseDTO> content, // 실제 데이터 리스트
  int totalPages,                          // 전체 페이지 수
  long totalElements,                       // 전체 데이터 개수
  int currentPage,                          // 현재 페이지 번호
  boolean isFirst,                          // 첫 페이지 여부
  boolean isLast                            // 마지막 페이지 여부
) {}
