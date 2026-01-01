package com.example.genie_tune_java.domain.product.service;

import com.example.genie_tune_java.domain.product.dto.ProductGetResponseDTO;
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
  public List<ProductGetResponseDTO> getAllProducts() {

    List<Product> list = productRepository.findAll();

    return list.stream().map(productMapper::toGetResponseDTO).toList();
  }
}
