package com.example.genie_tune_java.domain.webtoon.repository;

import com.example.genie_tune_java.domain.webtoon.entity.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {
}
