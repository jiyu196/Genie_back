package com.example.genie_tune_java.domain.word_rule.repository;

import com.example.genie_tune_java.domain.word_rule.entity.WordRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordRuleRepository extends JpaRepository<WordRule, Long> {

}
