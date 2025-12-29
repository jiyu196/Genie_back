package com.example.genie_tune_java.domain.admin.service;

import com.example.genie_tune_java.domain.admin.dto.manage_member.JoinApplyRequestDTO;
import com.example.genie_tune_java.domain.admin.dto.manage_member.JoinApplyResponseDTO;
import com.example.genie_tune_java.domain.admin.dto.manage_member.page.MemberPageResponse;
import com.example.genie_tune_java.domain.admin.dto.manage_member.page.MemberSearchCondition;

import java.awt.print.Pageable;

public interface RegisterRequestService {
  MemberPageResponse findAll(int page, int size, MemberSearchCondition condition);
  JoinApplyResponseDTO handleRegister(JoinApplyRequestDTO dto);
}
