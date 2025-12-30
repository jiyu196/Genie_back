package com.example.genie_tune_java.domain.pay.repository;

import com.example.genie_tune_java.domain.pay.entity.PayMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayMethodRepository extends JpaRepository<PayMethod,Long> {
}
