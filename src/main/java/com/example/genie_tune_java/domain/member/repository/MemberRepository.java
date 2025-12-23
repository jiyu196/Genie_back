package com.example.genie_tune_java.domain.member.repository;

import com.example.genie_tune_java.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
  Optional<Member> findByEmail(String email);
  Optional<Member> findByOrganizationName(String organizationName);

  boolean existsByEmail(String email);
  boolean existsByOrganizationName(String organizationName);

  boolean existsByBizNumber(String bizNumber);
}
