package com.example.genie_tune_java.domain.webtoon.repository;

import com.example.genie_tune_java.domain.webtoon.entity.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {
  @Query("SELECT w FROM Webtoon w " +
          "JOIN FETCH w.prompt p " +
          "WHERE p.serviceAccess.id = :serviceAccessId " +
          "ORDER BY w.createdAt DESC")
  List<Webtoon> findAllByServiceAccessId(@Param("serviceAccessId") Long serviceAccessId);
}
