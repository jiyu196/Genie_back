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

    // Order.totalAmount 는 내부 기준 금액 (실결제 금액 * 1000), 실제 포트원에 전달되는 금액은 저 금액의 1/1000
    Long totalAmount = (long) product.getPrice();


    //3. Order 객체 생성 및 Table에 저장
    String orderUuid = "ORD-" + UUID.randomUUID().toString();

    OrderInputDTO orderInputDTO = new OrderInputDTO(orderUuid, member, OrderStatus.PENDING, totalAmount);

    Order order = orderRepository.save(orderMapper.toEntity(orderInputDTO));

    //4. MakeOrderResponseDTO 반환(mapper 반환)
    return orderMapper.toMakeOrderResponseDTO(order);
  }

  @Transactional()
  public void updateOrderStatus(String orderUuid, OrderStatus orderStatus) {
    // 1. Uuid를 통한 order 객체 가져오기 (OrderUuid에 인덱스 걸어놨음, 비교적 성능 준수할 듯)
    Order order = orderRepository.findByOrderUuid(orderUuid).orElseThrow(() -> new GlobalException(ErrorCode.ORDER_NOT_FOUND));
    // 2. orderStatus를 바꾸는 엔티티 메서드 생성
    order.changeOrderStatus(orderStatus);

  }
}
