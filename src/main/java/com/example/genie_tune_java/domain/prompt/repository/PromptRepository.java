package com.example.genie_tune_java.domain.prompt.repository;

import com.example.genie_tune_java.domain.prompt.entity.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptRepository extends JpaRepository<Prompt, Long> {
}
