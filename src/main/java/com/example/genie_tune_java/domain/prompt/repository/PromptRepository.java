package com.example.genie_tune_java.domain.prompt.repository;

import com.example.genie_tune_java.domain.prompt.entity.Prompt;
import com.example.genie_tune_java.domain.service_access.entity.ServiceAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PromptRepository extends JpaRepository<Prompt, Long> {

  @Query("SELECT p FROM Prompt p " +
          "JOIN FETCH p.serviceAccess sa " +
          "WHERE p.originalContent = :originalContent " +
          "AND sa.accessId = :accessId " +
          "AND p.promptStatus = 'WAITING' " +
          "ORDER BY p.createdAt DESC " +
          "LIMIT 1")
  Optional<Prompt> findByOriginalContentAndServiceAccess(@Param("originalContent") String originalContent, @Param("accessId") String accessId);
}
