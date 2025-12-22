package com.example.genie_tune_java.domain.admin.dto.manage_member.page;

import com.example.genie_tune_java.domain.admin.entity.RegisterStatus;
import com.example.genie_tune_java.domain.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberSearchCondition {
  private MemberSearchType searchType;
  private String keyword;
  private RegisterStatus registerStatus;
  private Role role;
}
