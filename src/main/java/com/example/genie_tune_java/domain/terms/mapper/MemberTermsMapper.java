package com.example.genie_tune_java.domain.terms.mapper;

import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.terms.entity.MemberTerms;
import com.example.genie_tune_java.domain.terms.entity.Terms;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberTermsMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "agreedAt", ignore = true)
  MemberTerms toEntity(Member member, Terms terms);
}
