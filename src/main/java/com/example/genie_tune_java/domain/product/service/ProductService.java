package com.example.genie_tune_java.domain.product.service;

import com.example.genie_tune_java.domain.product.dto.ProductGetRequestDTO;
import com.example.genie_tune_java.domain.product.dto.ProductGetResponseDTO;
import com.example.genie_tune_java.domain.product.dto.ProductListResponseDTO;

import java.util.List;

public interface ProductService {
  ProductListResponseDTO getAllProducts();
  ProductGetResponseDTO getProduct(ProductGetRequestDTO dto);
}
