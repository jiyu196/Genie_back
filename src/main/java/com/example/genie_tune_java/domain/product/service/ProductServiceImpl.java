package com.example.genie_tune_java.domain.product.service;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.product.dto.ProductGetRequestDTO;
import com.example.genie_tune_java.domain.product.dto.ProductGetResponseDTO;
import com.example.genie_tune_java.domain.product.dto.ProductListResponseDTO;
import com.example.genie_tune_java.domain.product.entity.Product;
import com.example.genie_tune_java.domain.product.mapper.ProductMapper;
import com.example.genie_tune_java.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  @Override
  public ProductListResponseDTO getAllProducts() {
    List<Product> list = productRepository.findAll();
    return new ProductListResponseDTO(list.stream().map(productMapper::toGetResponseDTO).toList());
  }

  @Override
  public ProductGetResponseDTO getProduct(ProductGetRequestDTO dto) {
    return productMapper.toGetResponseDTO(productRepository.findById(dto.id()).orElseThrow(() -> new GlobalException(ErrorCode.PRODUCT_NOT_FOUND)));
  }
}
