package com.example.genie_tune_java.domain.product.controller;

import com.example.genie_tune_java.domain.product.dto.ProductGetRequestDTO;
import com.example.genie_tune_java.domain.product.dto.ProductGetResponseDTO;
import com.example.genie_tune_java.domain.product.dto.ProductListResponseDTO;
import com.example.genie_tune_java.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.Query;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ProductController {
  private final ProductService productService;

  @QueryMapping
  public ProductListResponseDTO getAllProducts() {
    return productService.getAllProducts();
  }

  @QueryMapping
  public ProductGetResponseDTO selectOne(@Argument("input") ProductGetRequestDTO dto) {
    return productService.getProduct(dto);
  }
}
