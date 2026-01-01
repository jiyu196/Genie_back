package com.example.genie_tune_java.domain.order.service;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.member.entity.AccountStatus;
import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.member.entity.RegisterStatus;
import com.example.genie_tune_java.domain.member.repository.MemberRepository;
import com.example.genie_tune_java.domain.order.dto.MakeOrderRequestDTO;
import com.example.genie_tune_java.domain.order.dto.MakeOrderResponseDTO;
import com.example.genie_tune_java.domain.order.repository.OrderRepository;
import com.example.genie_tune_java.domain.product.repository.ProductRepository;
import com.example.genie_tune_java.security.dto.JWTPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final MemberRepository memberRepository;
  private final ProductRepository productRepository;

  @Override
  @Transactional
  public MakeOrderResponseDTO makeOrder(MakeOrderRequestDTO dto) {
    //1. Member 조회
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    JWTPrincipal principal = (JWTPrincipal) authentication.getPrincipal();
    Member member = memberRepository.findById(principal.getMemberId()).orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

    if(member.getAccountStatus() != AccountStatus.ACTIVE || member.getRegisterStatus() != RegisterStatus.APPROVED) {
      throw new GlobalException(ErrorCode.ORDER_NOT_ALLOWED);
    }
    //2. 상품 조회
    if(!productRepository.existsById(dto.productId())) {
      throw new GlobalException(ErrorCode.PRODUCT_NOT_FOUND);
    }
    //3. 결제 호출 (외부 API 호출 Service 미구현, 추후 구현 예정)

    //4. 결제 성공 여부에 따른 구독 서비스 호출

    //5. 전부 성공시 Make OrderResponseDTO 객체 1개 생성

    return null;
  }
}
