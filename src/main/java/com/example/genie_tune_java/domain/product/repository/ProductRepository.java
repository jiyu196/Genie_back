package com.example.genie_tune_java.domain.product.repository;

import com.example.genie_tune_java.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
