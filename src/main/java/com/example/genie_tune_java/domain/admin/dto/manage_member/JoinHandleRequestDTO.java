package com.example.genie_tune_java.domain.admin.dto.manage_member;

import com.example.genie_tune_java.domain.admin.entity.RegisterStatus;
import com.example.genie_tune_java.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinHandleRequestDTO {
  Member member;
  String rejectReason;
  RegisterStatus registerStatus;
  LocalDateTime checkedAt;

  public static JoinHandleRequestDTO of(Member member, JoinApplyRequestDTO dto) {
    return new JoinHandleRequestDTO(
      member, dto.rejectReason(), dto.registerStatus(), LocalDateTime.now()
    );
  }
}
