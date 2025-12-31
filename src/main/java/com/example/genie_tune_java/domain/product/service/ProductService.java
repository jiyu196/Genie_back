package com.example.genie_tune_java.domain.product.service;

import com.example.genie_tune_java.domain.product.dto.ProductGetResponseDTO;

import java.util.List;

public interface ProductService {
  List<ProductGetResponseDTO> getAllProducts();
}
