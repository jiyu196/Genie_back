package com.example.genie_tune_java.domain.product.dto;

import java.util.List;

public record ProductListResponseDTO(
  List<ProductGetResponseDTO> list
) {}
