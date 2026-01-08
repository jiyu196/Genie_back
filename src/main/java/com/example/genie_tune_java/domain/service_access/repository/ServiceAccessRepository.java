package com.example.genie_tune_java.domain.service_access.repository;

import com.example.genie_tune_java.domain.service_access.entity.ServiceAccess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServiceAccessRepository extends JpaRepository<ServiceAccess, Long> {

  @Query("SELECT p.maxServiceAccessIdCount from Subscription s " +
          "LEFT JOIN s.order o " +
          "LEFT JOIN o.product p " +
          "WHERE s.id = :subscriptionId")
  Optional<Integer> findMaxServiceAccessIdCount(@Param("subscriptionId") Long subscriptionId);

  @EntityGraph(attributePaths = {"member"})
  @Query(value = "SELECT s FROM ServiceAccess s WHERE s.member.id = :memberId",
          countQuery = "SELECT count(s) FROM ServiceAccess s WHERE s.member.id = :memberId")
  Page<ServiceAccess> findAllMyServiceAccessByMemberId(@Param("memberId") Long memberId, Pageable pageable);

  Optional<ServiceAccess> findByEncryptedKey(String encryptedKey);

  Optional<ServiceAccess> findByAccessId(String accessId);
}
