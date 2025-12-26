package com.example.genie_tune_java.domain.member.repository;

import com.example.genie_tune_java.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
  // 로그인 시 Member 찾기
  Optional<Member> findByEmail(String email);
  // email 찾기 (사업자등록번호 + 담당자 명)
  Optional<Member> findByBizNumberAndContactName(String bizNumber, String contactName);

  // Email 중복 검사
  boolean existsByEmail(String email);

  // 기관명 중복검사
  boolean existsByOrganizationName(String organizationName);

  // 사업자등록번호 중복검사
  boolean existsByBizNumber(String bizNumber);

  Optional<Member> findByEmailAndBizNumberAndContactName(String email, String bizNumber, String contactName);
}
