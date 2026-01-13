package com.example.genie_tune_java.domain.pay.repository;


import com.example.genie_tune_java.domain.pay.entity.Pay;
import com.example.genie_tune_java.domain.pay.entity.PayStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PayRepository extends JpaRepository<Pay, Long> {
  @EntityGraph(attributePaths = {"order", "member"})
  @Query(value = "SELECT p FROM Pay p " +
          "WHERE p.member.id = :memberId",
          countQuery = "SELECT count(p) FROM Pay p  " +
                  "WHERE p.member.id = :memberId ")
  Page<Pay> findIndividualPayment(@Param("memberId") Long memberId, Pageable pageable);

  // N -> 1 1 -> 1 즉 합치는 table이 :1 인 구조로 되면 fetch를 해도 데이터가 뻥튀기가 되지 않음
  // ex) 만약 member 를 기준으로 join을 한다고 하면 member -> pay  이렇게 되면 하나의 member는 여러 개의 pay를 가질 수가 있다. 이러면, mebmer 1개에 pay 갯수만큼 row가 들어난다.
  // 이러면 fetch 후에 pageable을 할 때 spring JPA는 객체 기준 10개인지 DB행 기준 10개인지 모름
  @Query(value = """
        select p
        from Pay p
        join fetch p.member m
        join fetch p.order o
        join fetch o.product pr
        where
          (
            cast(:keyword as string) is null or cast(:keyword as string) = ''
            or (cast(:salesSearchType as string) = 'ORDER_ID' and o.orderUuid like concat('%', cast(:keyword as string ), '%'))
            or (cast(:salesSearchType as string) = 'ORGANIZATION_NAME' and m.organizationName like concat('%', cast(:keyword as string ), '%'))
          )
          and (cast(:payStatus as string) is null or p.payStatus = :payStatus)
          and (cast(:pgType as string) is null or p.pgType = :pgType)
          and (cast(:cardCompany as string) is null or p.cardCompany = :cardCompany)
          and (cast(:from as localdatetime) is null or p.updatedAt >= :from)
          and (cast(:to as localdatetime) is null or p.updatedAt <= :to)
          and (cast(:displayName as string) is null or pr.displayName = :displayName)
        order by p.updatedAt desc
        """,
          countQuery = """
        select count(p)
        from Pay p
        join p.order o
        join o.member m
        join o.product pr
        where
          (
            cast(:keyword as string) is null or cast(:keyword as string) = ''
            or (cast(:salesSearchType as string) = 'ORDER_ID' and o.orderUuid like concat('%', :keyword, '%'))
            or (cast(:salesSearchType as string) = 'ORGANIZATION_NAME' and m.organizationName like concat('%', :keyword, '%'))
          )
          and (cast(:payStatus as string) is null or p.payStatus = :payStatus)
          and (cast(:pgType as string) is null or p.pgType = :pgType)
          and (cast(:cardCompany as string) is null or p.cardCompany = :cardCompany)
          and (cast(:from as localdatetime) is null or p.updatedAt >= :from)
          and (cast(:to as localdatetime) is null or p.updatedAt <= :to)
          and (cast(:displayName as string) is null or pr.displayName = :displayName)
        """
  )
  Page<Pay> searchAdminSales(
          @Param("salesSearchType") String salesSearchType,
          @Param("keyword") String keyword,
          @Param("payStatus") PayStatus payStatus,
          @Param("pgType") String pgType,
          @Param("cardCompany") String cardCompany,
          @Param("from") LocalDateTime from,
          @Param("to") LocalDateTime to,
          @Param("displayName") String displayName,
          Pageable pageable
  );

}
