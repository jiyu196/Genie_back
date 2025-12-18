package com.example.genie_tune_java.domain.register_request.repository;

import com.example.genie_tune_java.domain.register_request.entity.RegisterRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterRequestRepository extends JpaRepository<RegisterRequest, Long> {
  RegisterRequest findByBizNumber(String businessNo);
}
