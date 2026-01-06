package com.example.genie_tune_java.application;

import com.example.genie_tune_java.api.port_one.dto.PortOnePreRegisterRequestDTO;
import com.example.genie_tune_java.api.port_one.service.PortOneService;
import com.example.genie_tune_java.common.exception.ErrorCode;
import com.example.genie_tune_java.common.exception.GlobalException;
import com.example.genie_tune_java.domain.order.dto.MakeOrderRequestDTO;
import com.example.genie_tune_java.domain.order.dto.MakeOrderResponseDTO;
import com.example.genie_tune_java.domain.order.entity.OrderStatus;
import com.example.genie_tune_java.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentPrepareFacade {
  //주문생성, 포트원 -> 사전등록 조립 여기서부터는 try/catch를 사용해서 흐름 제어를 해야함
  private final OrderService orderService;
  private final PortOneService portOneService;

  @Value("${PORTONE_STORE_ID}")
  private String storeId;
  @Value("${PORTONE_CHANNEL_KEY}")
  private String channelKey;

  public MakeOrderResponseDTO preparePayment (MakeOrderRequestDTO dto) {
    //1. 주문 생성
    MakeOrderResponseDTO originalResponse = orderService.makeOrder(dto);
    log.info("originalResponse: {}",originalResponse);
    log.info("originalRespone productId: {}",originalResponse.productId());
    PortOnePreRegisterRequestDTO preRegisterRequestDTO = new PortOnePreRegisterRequestDTO(storeId, originalResponse.totalAmount()/1000L, 0L, "KRW");
    //2. PortOne PreRegister 메서드 호출

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
