package com.example.genie_tune_java.domain.member.repository;

import com.example.genie_tune_java.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
