package com.example.genie_tune_java.domain.admin.service;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.admin.dto.manage_member.*;
import com.example.genie_tune_java.domain.admin.dto.manage_member.page.MemberPageResponse;
import com.example.genie_tune_java.domain.admin.dto.manage_member.page.MemberSearchCondition;
import com.example.genie_tune_java.domain.admin.dto.manage_member.page.MemberSearchType;
import com.example.genie_tune_java.domain.admin.dto.manage_subscription.*;
import com.example.genie_tune_java.domain.admin.entity.RegisterRequest;
import com.example.genie_tune_java.domain.admin.mapper.RegisterRequestMapper;
import com.example.genie_tune_java.domain.admin.repository.RegisterRequestRepository;
import com.example.genie_tune_java.domain.attach.entity.Attach;
import com.example.genie_tune_java.domain.attach.entity.AttachTargetType;
import com.example.genie_tune_java.domain.attach.repository.AttachRepository;
import com.example.genie_tune_java.domain.attach.service.AttachService;
import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.member.entity.RegisterStatus;
import com.example.genie_tune_java.domain.member.entity.Role;
import com.example.genie_tune_java.domain.member.repository.MemberRepository;
import com.example.genie_tune_java.domain.pay.entity.Pay;
import com.example.genie_tune_java.domain.pay.entity.PayStatus;
import com.example.genie_tune_java.domain.pay.mapper.PayMapper;
import com.example.genie_tune_java.domain.pay.repository.PayRepository;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final RegisterRequestRepository registerRequestRepository;
  private final RegisterRequestMapper registerRequestMapper;
  private final MemberRepository memberRepository;
  private final AttachRepository attachRepository;
  private final AttachService attachService;
  private final PayRepository payRepository;
  private final PayMapper payMapper;

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

    //4. repository를 이용해서 연결 RegisterRequest가 담긴 Page 객체 생성
    Page<RegisterRequest> entityPage = registerRequestRepository.findAllWithMember(typeString, keyword, status, role, pageable);

    //5. page 객체에서 attach 목록에 필요한 Member Id 뽑아오기
    List<Long> memberIds = entityPage.stream().map(rr -> rr.getMember().getId()).toList();
    List<AttachTargetType> targetTypes = List.of(AttachTargetType.MEMBER_BUSINESS, AttachTargetType.MEMBER_EMPLOYMENT);

    Map<Long, Map<AttachTargetType, String>> memberFileMap = attachRepository.findByAttachTargetTypeInAndTargetIdIn(targetTypes, memberIds).stream()
            .collect(Collectors.groupingBy(
                    Attach::getTargetId, // 첫 번째 키: Member ID
                    Collectors.toMap(
                            Attach::getAttachTargetType, // 두 번째 키: 파일 타입 (ENUM)
                            Attach::getS3Key, // value: URL(경로)
                            (existing, replacement) -> existing // 혹시 중복 데이터 있으면 기존 것 유지
                    )
            ));
    //7. Member Id와 type에 해당하는 첨부파일 전부 조회

    List<RegisterRequestResponseDTO> list = entityPage.stream().map(rr-> {
      // 7-1) Page 객체에서 RegisterRequest가 iterable 하므로 얘를 stream -> map 해서 mapper에 전부 mapping 다만, attach 주소는 여기에 포함 x
      RegisterRequestResponseDTO dto = registerRequestMapper.toDto(rr);
      // 7-2) Member Id 가져오기
      Long memberId = rr.getMember().getId();
      // 7-3) Member Id를 key 값으로 사용하여 value 값인 Map을 가져온다. ex) <MEMBER_BUSINESS, "S3Key"> 형태로 되어 있음
      Map<AttachTargetType, String> targetTypeMap = memberFileMap.getOrDefault(memberId, Map.of());

      dto.insertAttach(attachService.buildFileUrl(targetTypeMap.get(AttachTargetType.MEMBER_BUSINESS)),attachService.buildFileUrl(targetTypeMap.get(AttachTargetType.MEMBER_EMPLOYMENT)));
      return dto;
    }).toList();

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

  public AdminSalesPageResponseDTO findAllSales(AdminSalesPageRequestDTO dto) {
    //0. 자주쓰는 condition 객체 꺼내기
    SalesSearchCondition condition = dto.salesSearchCondition();
    //1. page index 조절
    int pageIndex = Math.max(dto.page() - 1, 0);

    //2. pageable 생성
    Pageable pageable = PageRequest.of(pageIndex, dto.size() ,Sort.by(Sort.Direction.DESC, "updatedAt"));

    //3. 날짜 변환
    LocalDateTime from = condition.from() != null ? LocalDateTime.parse(condition.from()) : null;
    LocalDateTime to = condition.to() != null ? LocalDateTime.parse(condition.to()) : null;

    //4. 카드 결제가 아니면 cardCompany 무시
    String cardCompany = (condition.pgType() != null && condition.pgType().equalsIgnoreCase("CARD")) ? condition.cardCompany() : null;

    //5. type, keyword, payStatus 관련 타입 형변환 처리
    String typeString = (condition.salesSearchType() != null) ? condition.salesSearchType().name() : SalesSearchType.ORGANIZATION_NAME.name();
    String keyword = (condition.keyword() != null) ? condition.keyword() : null;
    PayStatus payStatus = (condition.payStatus() != null) ? condition.payStatus() : null;

    //6. page 조회 (Repository)
    Page<Pay> pageResult = payRepository.searchAdminSales(
      typeString, keyword, payStatus, condition.pgType(), cardCompany, from, to, condition.displayName(), pageable);

    List<PayInfoResponseDTO> content = pageResult.getContent().stream().map(payMapper::toResponseForAdminPage).toList();

    return new AdminSalesPageResponseDTO(content, pageResult.getTotalPages(), pageResult.getTotalElements(), pageResult.getNumber() + 1 , pageResult.isFirst(), pageResult.isLast());
  }

  @Transactional
  public HandleStatusResponseDTO changeMemberStatus(HandleStatusRequestDTO dto) {
    Member member = memberRepository.findByEmail(dto.email()).orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
    Role role =  dto.role().equals("MEMBER") ? Role.MEMBER : Role.ADMIN;
    member.changeMemberRole(role);
    return new HandleStatusResponseDTO(true);
  }
}
