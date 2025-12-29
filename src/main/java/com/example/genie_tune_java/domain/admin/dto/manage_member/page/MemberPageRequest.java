package com.example.genie_tune_java.domain.admin.dto.manage_member.page;

public record MemberPageRequest(
  int page, int size, MemberSearchCondition condition
) {}
