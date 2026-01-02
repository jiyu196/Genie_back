package com.example.genie_tune_java.domain.order.service;

import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.member.entity.AccountStatus;
import com.example.genie_tune_java.domain.member.entity.Member;
import com.example.genie_tune_java.domain.member.entity.RegisterStatus;
import com.example.genie_tune_java.domain.member.repository.MemberRepository;
import com.example.genie_tune_java.domain.order.dto.MakeOrderRequestDTO;
import com.example.genie_tune_java.domain.order.dto.MakeOrderResponseDTO;
import com.example.genie_tune_java.domain.order.dto.OrderInputDTO;
import com.example.genie_tune_java.domain.order.entity.Order;
import com.example.genie_tune_java.domain.order.entity.OrderStatus;
import com.example.genie_tune_java.domain.order.mapper.OrderMapper;
import com.example.genie_tune_java.domain.order.repository.OrderRepository;
import com.example.genie_tune_java.domain.product.entity.Product;
import com.example.genie_tune_java.domain.product.repository.ProductRepository;
import com.example.genie_tune_java.security.dto.JWTPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;
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

    //2. 상품 조회 및 Order 객체에 필요한 값 꺼내기
    Product product = productRepository.findById(dto.productId()).orElseThrow(() -> new GlobalException(ErrorCode.PRODUCT_NOT_FOUND));

    Long totalAmount = (long) product.getPrice();

    //3. Order 객체 생성 및 Table에 저장
    String orderUuid = "ORD-" + UUID.randomUUID().toString();

    OrderInputDTO orderInputDTO = new OrderInputDTO(orderUuid, member, OrderStatus.PENDING, totalAmount);

    Order order = orderRepository.save(orderMapper.toEntity(orderInputDTO));

    //4. MakeOrderResponseDTO 반환(mapper 반환)
    return orderMapper.toMakeOrderResponseDTO(order);
  }
}
