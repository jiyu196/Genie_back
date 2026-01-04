package com.example.genie_tune_java.domain.product.mapper;

import com.example.genie_tune_java.domain.product.dto.ProductGetResponseDTO;
import com.example.genie_tune_java.domain.product.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  ProductGetResponseDTO toGetResponseDTO(Product product);
}
