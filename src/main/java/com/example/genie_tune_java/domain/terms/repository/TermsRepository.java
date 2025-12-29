package com.example.genie_tune_java.domain.terms.repository;

import com.example.genie_tune_java.domain.terms.entity.Terms;
import com.example.genie_tune_java.domain.terms.entity.TermsCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TermsRepository extends CrudRepository<Terms, Long> {

  @Query("SELECT t FROM Terms t WHERE t.termsCategory = :termsCategory " +
          "AND t.termsStatus = 'ACTIVE' ORDER BY t.version DESC LIMIT 1")
  Optional<Terms> findLatestByCategory(@Param("termsCategory") TermsCategory termsCategory);
}
