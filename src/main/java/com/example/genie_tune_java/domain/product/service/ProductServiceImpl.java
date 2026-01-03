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
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  @Override
  public ProductListResponseDTO getAllProducts() {
    List<Product> list = productRepository.findAll();

    log.info("현재 담긴 상품 종류 : {}", list);
    list.stream().map(productMapper::toGetResponseDTO).toList().forEach(log::info);
    return new ProductListResponseDTO(list.stream().map(productMapper::toGetResponseDTO).toList());
  }

  @Override
  public ProductGetResponseDTO getProduct(ProductGetRequestDTO dto) {
    return productMapper.toGetResponseDTO(productRepository.findById(dto.id()).orElseThrow(() -> new GlobalException(ErrorCode.PRODUCT_NOT_FOUND)));
  }
}
