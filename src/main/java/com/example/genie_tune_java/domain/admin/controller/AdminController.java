package com.example.genie_tune_java.domain.admin.controller;

import com.example.genie_tune_java.domain.admin.dto.manage_member.RegisterRequestResponseDTO;
import com.example.genie_tune_java.domain.admin.dto.manage_member.page.MemberPageResponse;
import com.example.genie_tune_java.domain.admin.dto.manage_member.page.MemberSearchCondition;
import com.example.genie_tune_java.domain.admin.service.RegisterRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class AdminController {

  private final RegisterRequestService registerRequestService;

  @QueryMapping
  public MemberPageResponse getAllMembers(@Argument int page, @Argument int size, @Argument MemberSearchCondition condition) {
    return registerRequestService.findAll(page, size, condition);
  }
}
