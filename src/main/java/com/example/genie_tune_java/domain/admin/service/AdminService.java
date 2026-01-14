package com.example.genie_tune_java.domain.admin.service;

import com.example.genie_tune_java.domain.admin.dto.manage_member.HandleStatusRequestDTO;
import com.example.genie_tune_java.domain.admin.dto.manage_member.HandleStatusResponseDTO;
import com.example.genie_tune_java.domain.admin.dto.manage_member.JoinApplyRequestDTO;
import com.example.genie_tune_java.domain.admin.dto.manage_member.JoinApplyResponseDTO;
import com.example.genie_tune_java.domain.admin.dto.manage_member.page.MemberPageResponse;
import com.example.genie_tune_java.domain.admin.dto.manage_member.page.MemberSearchCondition;
import com.example.genie_tune_java.domain.admin.dto.manage_subscription.AdminSalesPageRequestDTO;
import com.example.genie_tune_java.domain.admin.dto.manage_subscription.AdminSalesPageResponseDTO;

public interface AdminService {
  MemberPageResponse findAll(int page, int size, MemberSearchCondition condition);
  JoinApplyResponseDTO handleRegister(JoinApplyRequestDTO dto);
  AdminSalesPageResponseDTO findAllSales(AdminSalesPageRequestDTO dto);
  public HandleStatusResponseDTO changeMemberStatus(HandleStatusRequestDTO dto);
}
