package com.example.genie_tune_java.domain.pay.service;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.pay.dto.pageable.GetPaymentResponseDTO;
import com.example.genie_tune_java.domain.pay.dto.pageable.MyPaymentPageResponse;
import com.example.genie_tune_java.domain.pay.dto.success.PaySuccessRegisterInputDTO;
import com.example.genie_tune_java.domain.pay.dto.success.PaySuccessRegisterOutputDTO;
import com.example.genie_tune_java.domain.pay.entity.Pay;
import com.example.genie_tune_java.domain.pay.entity.PayMethod;
import com.example.genie_tune_java.domain.pay.mapper.PayMapper;
import com.example.genie_tune_java.domain.pay.repository.PayMethodRepository;
import com.example.genie_tune_java.domain.pay.repository.PayRepository;
import com.example.genie_tune_java.security.dto.JWTPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class PayServiceImpl implements PayService {
  private final PayRepository payRepository;
  private final PayMethodRepository payMethodRepository;
  private final PayMapper payMapper;

  @Override
  @Transactional
  public PaySuccessRegisterOutputDTO paySuccessRegister(PaySuccessRegisterInputDTO dto) {
    Pay pay = payMapper.toSuccessEntity(dto);
    payRepository.save(pay);
    return payMapper.toSuccessDto(pay);
  }

  @Override
  @Transactional(readOnly = true)
  public MyPaymentPageResponse findIndividualAll(int page, int size) {

    // 1. 프론트에서 page index를 1부터 시작하도록 편의성
    int pageIndex = (page > 0) ? page - 1 : 0;

    // 2. 최신 결제 내역순으로 Pageable 객체 생성
    Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "updatedAt"));

    //3. 인증 객체에서 Member 정보 가져오기
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    JWTPrincipal jwtPrincipal = (JWTPrincipal) authentication.getPrincipal();
    Long memberId = jwtPrincipal.getMemberId();

    //4. Repository를 활용하여 정보 가져오기
    //4-1. 기본 pay 정보 + order/member fetch join
    Page<Pay> payPage = payRepository.findIndividualPayment(memberId, pageable);
    //4-2. payMethod 정보
    PayMethod myPayMethod = payMethodRepository.findByMemberId(memberId).orElseThrow(() -> new GlobalException(ErrorCode.PAYMETHOD_NOT_FOUND));

    //5. 리스트에서 Mapping 시작

    List<GetPaymentResponseDTO> content = payPage.stream().map(pay -> payMapper.toGetPaymentDto(pay, myPayMethod)).toList();

    return new MyPaymentPageResponse(content, payPage.getTotalPages(), payPage.getTotalElements(), payPage.getNumber() + 1, payPage.isFirst(), payPage.isLast());
  }
}
