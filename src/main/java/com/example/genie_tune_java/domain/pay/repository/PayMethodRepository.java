package com.example.genie_tune_java.domain.pay.repository;

import com.example.genie_tune_java.domain.pay.entity.PayMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PayMethodRepository extends JpaRepository<PayMethod,Long> {
  @Query("SELECT pm FROM PayMethod pm " +
          "JOIN FETCH pm.member m " +
          "WHERE m.id = :memberId ")
  Optional<PayMethod> findByMemberId(@Param("memberId") Long memberId);
}
