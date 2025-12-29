package com.example.genie_tune_java.domain.terms.repository;

import com.example.genie_tune_java.domain.terms.entity.MemberTerms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTermsRepository extends JpaRepository<MemberTerms,Long> {
}
