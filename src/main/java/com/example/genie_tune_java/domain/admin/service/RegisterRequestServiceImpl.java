package com.example.genie_tune_java.domain.admin.service;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.admin.dto.manage_member.JoinApplyRequestDTO;
import com.example.genie_tune_java.domain.admin.dto.manage_member.JoinApplyResponseDTO;
import com.example.genie_tune_java.domain.admin.dto.manage_member.page.MemberPageResponse;
import com.example.genie_tune_java.domain.admin.dto.manage_member.RegisterRequestResponseDTO;
import com.example.genie_tune_java.domain.admin.dto.manage_member.page.MemberSearchCondition;
import com.example.genie_tune_java.domain.admin.dto.manage_member.page.MemberSearchType;
import com.example.genie_tune_java.domain.admin.entity.RegisterRequest;
import com.example.genie_tune_java.domain.admin.mapper.RegisterRequestMapper;
import com.example.genie_tune_java.domain.admin.repository.RegisterRequestRepository;
import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.member.entity.RegisterStatus;
import com.example.genie_tune_java.domain.member.entity.Role;
import com.example.genie_tune_java.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class RegisterRequestServiceImpl implements RegisterRequestService {

  private final RegisterRequestRepository registerRequestRepository;
  private final RegisterRequestMapper registerRequestMapper;
  private final MemberRepository memberRepository;

  @Override
  @Transactional(readOnly = true)
  public MemberPageResponse findAll(int page, int size, MemberSearchCondition condition) {
    // 1. 프론트에서 page index를 1부터 시작하도록 편의성
    int pageIndex = (page > 0) ? page - 1 : 0;
    // 2. 최신 가입 신청순으로 Pageable 객체 생성
    Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "createdAt"));

    // 3. condition DTO에서 값들을 직접 꺼내서 mapping
    String typeString = (condition != null) ? condition.getMemberSearchType().name() : MemberSearchType.ALL.name();
    String keyword = (condition != null) ? condition.getKeyword() : null;
    RegisterStatus status = (condition != null) ? condition.getRegisterStatus() : null;
    Role role = (condition != null) ? condition.getRole() : null;

    //4. repository를 이용해서 연결
    Page<RegisterRequest> entityPage = registerRequestRepository.findAllWithMember(typeString, keyword, status, role, pageable);
    List<RegisterRequestResponseDTO> list = entityPage.stream().map(registerRequestMapper::toDto).toList();

    return new MemberPageResponse(list, entityPage.getTotalPages(), entityPage.getTotalElements(), entityPage.getNumber() + 1,
            entityPage.isFirst(), entityPage.isLast());
  }

  @Override
  @Transactional
  public JoinApplyResponseDTO handleRegister(JoinApplyRequestDTO dto) {
    if(dto == null || dto.email() == null || dto.registerStatus() == null) {
      throw new GlobalException(ErrorCode.REQUIRED_FIELD_MISSING);
    }
    //0. status가 rejected인데 rejectReason이 없는 경우는 에러 투척
    if(dto.registerStatus() == RegisterStatus.REJECTED && dto.rejectReason() == null) {
      throw new GlobalException(ErrorCode.REQUIRED_FIELD_MISSING);
    }
    //1. email을 통한 member 값 조회
    Member member = memberRepository.findByEmail(dto.email()).orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

    //2. member entity 내 등록 상태 변경 메서드를 통해 등록상태 변경
    member.changeRegisterStatus(dto.registerStatus());

    //3. JoinHandlerRequest 가져오기
    RegisterRequest rr = registerRequestRepository.findByMember(member).orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

    //4. RegisterRequest 내에 있는 엔티티 메서드 활용 어차피
    rr.handleRegisterRequest(dto.registerStatus(), dto.rejectReason());

    return new JoinApplyResponseDTO(true);
  }
}
