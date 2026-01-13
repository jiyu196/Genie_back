package com.example.genie_tune_java.domain.admin.controller;

import com.example.genie_tune_java.domain.admin.dto.manage_member.JoinApplyRequestDTO;
import com.example.genie_tune_java.domain.admin.dto.manage_member.JoinApplyResponseDTO;
import com.example.genie_tune_java.domain.admin.dto.manage_member.page.MemberPageRequest;
import com.example.genie_tune_java.domain.admin.dto.manage_member.page.MemberPageResponse;
import com.example.genie_tune_java.domain.admin.dto.manage_subscription.AdminSalesPageRequestDTO;
import com.example.genie_tune_java.domain.admin.dto.manage_subscription.AdminSalesPageResponseDTO;
import com.example.genie_tune_java.domain.admin.service.AdminService;
import com.example.genie_tune_java.security.util.authorize.IsAdminUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@Log4j2
@RequiredArgsConstructor
public class AdminController {

  private final AdminService adminService;

  @QueryMapping
  @IsAdminUser
  public MemberPageResponse getAllMembers(@Argument("input") MemberPageRequest dto) {
    return adminService.findAll(dto.page(), dto.size(), dto.condition());
  }

  @MutationMapping
  @IsAdminUser
  public JoinApplyResponseDTO handleRegister(@Argument("input") JoinApplyRequestDTO dto) {
    return adminService.handleRegister(dto);
  }

  @QueryMapping
  @IsAdminUser
  public AdminSalesPageResponseDTO getAllSales(@Argument("input") AdminSalesPageRequestDTO dto) {
    return adminService.findAllSales(dto);
  }

}
