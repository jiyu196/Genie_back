package com.example.genie_tune_java.application.payment;

import com.example.genie_tune_java.api.port_one.dto.PortOnePreRegisterRequestDTO;
import com.example.genie_tune_java.api.port_one.service.PortOneService;
import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.order.dto.MakeOrderRequestDTO;
import com.example.genie_tune_java.domain.order.dto.MakeOrderResponseDTO;
import com.example.genie_tune_java.domain.order.entity.Order;
import com.example.genie_tune_java.domain.order.entity.OrderStatus;
import com.example.genie_tune_java.domain.order.repository.OrderRepository;
import com.example.genie_tune_java.domain.order.service.OrderService;
import com.example.genie_tune_java.domain.subscription.service.SubscriptionService;
import com.example.genie_tune_java.security.dto.JWTPrincipal;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentPrepareFacade {
  //주문생성, 포트원 -> 사전등록 조립 여기서부터는 try/catch를 사용해서 흐름 제어를 해야함
  private final OrderService orderService;
  private final SubscriptionService subscriptionService;
  private final PortOneService portOneService;

  @Value("${PORTONE_STORE_ID}")
  private String storeId;
  @Value("${PORTONE_CHANNEL_KEY}")
  private String channelKey;

  public MakeOrderResponseDTO preparePayment (MakeOrderRequestDTO dto) {

    //1. 현재 구독내역 있으면 -> 결제 차단 -> 구독중인 상품이 있습니다 안내
    if(subscriptionService.checkSubscription()) {
      throw new GlobalException(ErrorCode.SUBSCRIPTION_EXIST);
    }
    //2. 기존에 동일한 상품으로 'PENDING'이 되어 있을 경우 새로 만들지 않고 가져오기가 맞을 듯.

    Optional<Order> optionalOrder = orderService.checkOrder(dto);

    MakeOrderResponseDTO originalResponse;

    if(optionalOrder.isPresent()) {
      log.info("기존에 주문이력이 존재합니다.");
      Order order = optionalOrder.get();
      originalResponse = new MakeOrderResponseDTO(order.getOrderUuid(),order.getOrderStatus(), order.getTotalAmount(),
              order.getCreatedAt().toString(), order.getMember().getOrganizationName(), storeId, channelKey, order.getProduct().getId(), order.getProduct().getDisplayName());
    } else{
      log.info("최초 주문입니다.");
      originalResponse = orderService.makeOrder(dto);
      log.info("originalResponse: {}",originalResponse);
      log.info("originalRespone productId: {}",originalResponse.productId());
    }
    //3. PortOne PreRegister에 필요한 정보를 담은 DTO 생성
      PortOnePreRegisterRequestDTO preRegisterRequestDTO = new PortOnePreRegisterRequestDTO(storeId, originalResponse.totalAmount()/1000L, 0L, "KRW");

    //4. PortOne PreRegister 메서드 호출
    try {

      portOneService.preRegister(originalResponse.orderUuid(), preRegisterRequestDTO);

      log.info("portone 등록 성공");
      MakeOrderResponseDTO newResponse = new MakeOrderResponseDTO(
              originalResponse.orderUuid(), originalResponse.orderStatus(),
              originalResponse.totalAmount()/1000L, originalResponse.createdAt(), originalResponse.organizationName(), storeId, channelKey, originalResponse.productId(), originalResponse.displayName());
      log.info("newResponse: {}",newResponse);
      return newResponse;

    } catch (GlobalException e) {
      log.error("PreRegister 실패, 기존 Order 상태를 CANCELED로 변경합니다. [{}] {} ", e.getErrorCode(),e.getMessage());
      orderService.updateOrderStatus(originalResponse.orderUuid(), OrderStatus.CANCELED);
      //기존 PortOneService에서 만들어진 error 그대로 던짐
      throw e;

    } catch (Exception e) {
      log.error("알수 없는 error로 PreRegister 실패 {}", e.getMessage());
      orderService.updateOrderStatus(originalResponse.orderUuid(), OrderStatus.CANCELED);
      throw new GlobalException(ErrorCode.INTERNAL_ERROR);
    }
  }
}
