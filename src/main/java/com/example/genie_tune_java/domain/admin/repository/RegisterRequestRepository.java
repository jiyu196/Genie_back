package com.example.genie_tune_java.domain.admin.repository;

import com.example.genie_tune_java.domain.admin.dto.manage_member.page.MemberSearchType;
import com.example.genie_tune_java.domain.admin.entity.RegisterRequest;
import com.example.genie_tune_java.domain.admin.entity.RegisterStatus;
import com.example.genie_tune_java.domain.member.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface RegisterRequestRepository extends JpaRepository<RegisterRequest, Long> {
  // Member 정보를 한 번에 묶어서(JOIN) 가져오는 쿼리
  @Query(value = "SELECT rr FROM RegisterRequest rr JOIN FETCH rr.member m " +
          "WHERE (:status IS NULL OR rr.registerStatus = :status) " +
          "AND (:role IS NULL OR m.role = :role) " +
          "AND (" +
          "  :keyword IS NULL OR :keyword = '' OR " + // 키워드가 없거나 빈 문자열이면 통과
          "  (:type = 'BIZ_NUMBER' AND m.bizNumber LIKE %:keyword%) OR " +
          "  (:type = 'EMAIL' AND m.email LIKE %:keyword%) OR " +
          "  (:type = 'ORGANIZATION' AND m.organizationName LIKE %:keyword%) OR " +
          "  (:type = 'REPRESENTATIVE_NAME' AND m.representativeName LIKE %:keyword%) OR " + // 끝에 OR 제거
          "  ((:type IS NULL OR :type = 'ALL') AND (" +
          "    m.bizNumber LIKE %:keyword% OR " +
          "    m.email LIKE %:keyword% OR " +
          "    m.organizationName LIKE %:keyword% OR " +
          "    m.representativeName LIKE %:keyword%" + // 통합 검색에도 추가
          "  ))" +
          ")",
          countQuery = "SELECT count(rr) FROM RegisterRequest rr JOIN rr.member m " +
                  "WHERE (:status IS NULL OR rr.registerStatus = :status) " +
                  "AND (:role IS NULL OR m.role = :role)")
  Page<RegisterRequest> findAllWithMember(
          @Param("type") MemberSearchType type,
          @Param("keyword") String keyword,
          @Param("status") RegisterStatus status,
          @Param("role") Role role,
          Pageable pageable);
}
