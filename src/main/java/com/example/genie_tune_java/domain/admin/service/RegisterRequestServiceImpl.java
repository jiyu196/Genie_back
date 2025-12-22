package com.example.genie_tune_java.domain.admin.service;

import com.example.genie_tune_java.domain.admin.dto.manage_member.JoinApplyRequestDTO;
import com.example.genie_tune_java.domain.admin.dto.manage_member.page.MemberPageResponse;
import com.example.genie_tune_java.domain.admin.dto.manage_member.RegisterRequestResponseDTO;
import com.example.genie_tune_java.domain.admin.dto.manage_member.page.MemberSearchCondition;
import com.example.genie_tune_java.domain.admin.dto.manage_member.page.MemberSearchType;
import com.example.genie_tune_java.domain.admin.entity.RegisterRequest;
import com.example.genie_tune_java.domain.admin.entity.RegisterStatus;
import com.example.genie_tune_java.domain.admin.mapper.RegisterRequestMapper;
import com.example.genie_tune_java.domain.admin.repository.RegisterRequestRepository;
import com.example.genie_tune_java.domain.member.entity.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class RegisterRequestServiceImpl implements RegisterRequestService {

  private final RegisterRequestRepository registerRequestRepository;
  private final RegisterRequestMapper registerRequestMapper;

  @Override
  @Transactional(readOnly = true)
  public MemberPageResponse findAll(int page, int size, MemberSearchCondition condition) {
    // 1. 프론트에서 page index를 1부터 시작하도록 편의성
    int pageIndex = (page > 0) ? page - 1 : 0;
    // 2. 최신 가입 신청순으로 Pageable 객체 생성
    Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "createdAt"));

    // 3. condition DTO에서 값들을 직접 꺼내서 mapping
    MemberSearchType type = (condition != null) ? condition.getSearchType() : null;
    String keyword = (condition != null) ? condition.getKeyword() : null;
    RegisterStatus status = (condition != null) ? condition.getRegisterStatus() : null;
    Role role = (condition != null) ? condition.getRole() : null;

    //4. repository를 이용해서 연결
    Page<RegisterRequest> entityPage = registerRequestRepository.findAllWithMember(type, keyword, status, role, pageable);
    List<RegisterRequestResponseDTO> list = entityPage.stream().map(registerRequestMapper::toDto).toList();

    return new MemberPageResponse(list, entityPage.getTotalPages(), entityPage.getTotalElements(), entityPage.getNumber() + 1,
            entityPage.isFirst(), entityPage.isLast());
  }

  @Override
  public boolean handleRegister(JoinApplyRequestDTO dto) {
    return false;
  }
}
