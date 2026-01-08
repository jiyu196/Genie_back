package com.example.genie_tune_java.domain.service_access.service;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.service_access.dto.ServiceAccessRegisterInputDTO;
import com.example.genie_tune_java.domain.service_access.dto.pageable.GetMyAccessIdResponseDTO;
import com.example.genie_tune_java.domain.service_access.dto.pageable.MyAccessIdPageRequestDTO;
import com.example.genie_tune_java.domain.service_access.dto.pageable.MyAccessIdPageResponseDTO;
import com.example.genie_tune_java.domain.service_access.entity.ServiceAccess;
import com.example.genie_tune_java.domain.service_access.mapper.ServiceAccessMapper;
import com.example.genie_tune_java.domain.service_access.repository.ServiceAccessRepository;
import com.example.genie_tune_java.security.dto.JWTPrincipal;
import com.example.genie_tune_java.security.util.AESUtil;
import com.example.genie_tune_java.security.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class ServiceAccessServiceImpl implements ServiceAccessService {
  private final ServiceAccessRepository serviceAccessRepository;
  private final ServiceAccessMapper serviceAccessMapper;
  private final PasswordEncoder passwordEncoder;
  private final AESUtil aesUtil;

  @Override
  @Transactional
  public void issueServiceAccess(ServiceAccessRegisterInputDTO dto) {

    Integer maxServiceAccessIdCount = serviceAccessRepository.findMaxServiceAccessIdCount(dto.subscription().getId()).orElseThrow(() -> new GlobalException(ErrorCode.PRODUCT_MAX_PROMPT_DAILY_COUNT_NOT_FOUNT));
    List<ServiceAccess> serviceAccessList = new ArrayList<>();

    for(int i=0; i<maxServiceAccessIdCount; i++) {
    ServiceAccess serviceAccess = serviceAccessMapper.toIssueServiceAccess(dto);
      String serviceAccessIdOriginal = "SAID-" + UUID.randomUUID();

      String encryptedKey = aesUtil.encrypt(serviceAccessIdOriginal);

      String accessHash = passwordEncoder.encode(serviceAccessIdOriginal);

      String accessId = serviceAccessIdOriginal.substring(0, 12);

      serviceAccess.inputAccessId(accessId, accessHash, encryptedKey); // 이거 순서 바뀌어져 있었음 ㅋㅋ...
      serviceAccess.applySubscriptionPeriod(dto.subscription().getStartDate(), dto.subscription().getEndDate());

      serviceAccessList.add(serviceAccess);
    }
      serviceAccessRepository.saveAll(serviceAccessList);
  }

  @Override
  @Transactional(readOnly = true)
  public MyAccessIdPageResponseDTO getMyAccessIdPage(MyAccessIdPageRequestDTO dto) {

    //1. 프론트에서 page index 1부터 시작하도록 조절
    int pageIndex = (dto.page() > 0) ? dto.page() - 1 : 0;

    //2. 인증 객체에서 Member 정보 꺼내기
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    JWTPrincipal jwtPrincipal = (JWTPrincipal) authentication.getPrincipal();
    Long memberId = jwtPrincipal.getMemberId();

    Pageable pageable = PageRequest.of(pageIndex, dto.size(), Sort.by(Sort.Direction.DESC, "id"));

    Page<ServiceAccess> serviceAccessPage = serviceAccessRepository.findAllMyServiceAccessByMemberId(memberId, pageable);

    List<GetMyAccessIdResponseDTO> content = serviceAccessPage.stream().map(serviceAccess -> serviceAccessMapper.toGetMyAccessIdResponse(serviceAccess, aesUtil)).toList();

    return new MyAccessIdPageResponseDTO(content, serviceAccessPage.getTotalPages(), serviceAccessPage.getTotalElements(), serviceAccessPage.getNumber() + 1, serviceAccessPage.isFirst(), serviceAccessPage.isLast());
  }
}
