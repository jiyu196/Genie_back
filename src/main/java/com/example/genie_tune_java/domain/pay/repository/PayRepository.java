package com.example.genie_tune_java.domain.pay.repository;

import com.example.genie_tune_java.domain.pay.entity.Pay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PayRepository extends JpaRepository<Pay, Long> {
  @EntityGraph(attributePaths = {"order", "member"})
  @Query(value = "SELECT p FROM Pay p " +
          "WHERE p.member.id = :memberId",
          countQuery = "SELECT count(p) FROM Pay p  " +
                  "WHERE p.member.id = :memberId ")
  Page<Pay> findIndividualPayment(@Param("memberId") Long memberId, Pageable pageable);
}
