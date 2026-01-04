package com.example.genie_tune_java.domain.pay.repository;

import com.example.genie_tune_java.domain.pay.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Pay, Long> {
}
