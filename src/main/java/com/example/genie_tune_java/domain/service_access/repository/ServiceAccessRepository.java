package com.example.genie_tune_java.domain.service_access.repository;

import com.example.genie_tune_java.domain.service_access.entity.ServiceAccess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceAccessRepository extends JpaRepository<ServiceAccess, Long> {
}
