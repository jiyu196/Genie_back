package com.example.genie_tune_java.domain.order.repository;

import com.example.genie_tune_java.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
