package com.example.genie_tune_java.domain.prompt.repository;

import com.example.genie_tune_java.domain.prompt.entity.PromptLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptLogRepository extends JpaRepository<PromptLog,Long> {
}
