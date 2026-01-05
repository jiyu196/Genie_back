package com.example.genie_tune_java.application;

import com.example.genie_tune_java.api.port_one.dto.PortOneGetPaymentsRequestDTO;
import com.example.genie_tune_java.api.port_one.dto.PortOneGetPaymentsResponseDTO;
import com.example.genie_tune_java.api.port_one.service.PortOneService;
import com.example.genie_tune_java.domain.order.entity.Order;
import com.example.genie_tune_java.domain.order.entity.OrderStatus;
import com.example.genie_tune_java.domain.order.service.OrderService;
import com.example.genie_tune_java.domain.pay.dto.PaymentCheckRequestDTO;
import com.example.genie_tune_java.domain.pay.dto.PaymentCheckResponseDTO;
import com.example.genie_tune_java.domain.pay.dto.payment_method.PayMethodRegisterInputDTO;
import com.example.genie_tune_java.domain.pay.dto.payment_method.PaymentMethod;
import com.example.genie_tune_java.domain.pay.dto.success.PaySuccessRegisterInputDTO;
import com.example.genie_tune_java.domain.pay.dto.success.PaySuccessRegisterOutputDTO;
import com.example.genie_tune_java.domain.pay.entity.PayMethod;
import com.example.genie_tune_java.domain.pay.entity.PayStatus;
import com.example.genie_tune_java.domain.pay.service.PayMethodService;
import com.example.genie_tune_java.domain.pay.service.PayService;
import com.example.genie_tune_java.domain.subscription.dto.SubscriptionRegisterInputDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentConfirmFacade {
  private final PortOneService portOneService;
  private final PayMethodService payMethodService;
  private final PayService payService;
  private final OrderService orderService;
  @Value("${PORTONE_STORE_ID}")
  private static String storeId;

  public PaymentCheckResponseDTO checkPayment(PaymentCheckRequestDTO dto) {
    //1. 프론트에서 보내준 DTO 값으로 PortOne의 결제정보를 가져올 때 필요한 Record Type의 DTO로 변환
    PortOneGetPaymentsRequestDTO paymentsRequestDTO = new PortOneGetPaymentsRequestDTO(dto.paymentId(), storeId);
    //2. PortOne 결제 조회 서비스 호출(단일 결제 건) -> 얘는 Transactional 있으면 안됨.
    PortOneGetPaymentsResponseDTO paymentsResponseDTO = portOneService.getPayments(paymentsRequestDTO);
    log.info("payments: {} {}", paymentsResponseDTO.id(), paymentsResponseDTO.transactionId());
    String status = paymentsResponseDTO.status();
    log.info("결제 상태: {}", status);
    //4. 결제 조회 내용으로 Table Mapping 별도 클래스 파일 메서드로 빼서 여기만 Transactional 부여
     switch(status) {
       case "PAID" -> {
         //OrderService로 기존 Order 객체 변경, PayService 및 PayMethodService를 통해 pay table 등록, pay Method 등록
         //1. OrderService changeStatus 호출
         Order order = orderService.updateOrderStatus(dto.paymentId(), OrderStatus.SUCCESS);

         //2. PayService 호출 (성공 Register)
         PaySuccessRegisterInputDTO paySuccessRegisterInputDTO = new PaySuccessRegisterInputDTO(order, order.getMember(), PayStatus.PAID, paymentsResponseDTO.amount().total(), paymentsResponseDTO.paidPayment());
         PaySuccessRegisterOutputDTO paySuccessRegisterOutputDTO = payService.paySuccessRegister(paySuccessRegisterInputDTO);

         //3. PayMethodService 호출 (성공 Register), 정기결제 위해서 추후 BillingKey 호출하는 것도 필요함.
         PaymentMethod paymentMethod = paymentsResponseDTO.paymentMethod();
         PayMethodRegisterInputDTO payMethodRegisterInputDTO = new PayMethodRegisterInputDTO(order.getMember(), paymentMethod.pgType(),paymentMethod.paymentMethodCard().card().publisher(),paymentMethod.paymentMethodCard().card().number());
         PayMethod payMethod = payMethodService.registerPayMethod(payMethodRegisterInputDTO); //반환 값 PayMethod -> 아래 SubsCription이 사용함

         //4. SubscriptionService 호출
         SubscriptionRegisterInputDTO subscriptionRegisterInputDTO = new SubscriptionRegisterInputDTO(order.getMember(), order, order.getProduct(), payMethod);


         //5. AccessIdService 호출

       }

       case "PAY_PENDING" -> {
         // 주로 가상계좌(Virtual Account) 결제 시 발생
         // 주문 상태를 '입금 대기'로 변경하고, 입금 기한/계좌번호 정보를 DB에 저장

       }
       case "FAILED" -> {
         // 결제 실패 사유(failureReason)를 로그에 남기고 주문을 '실패' 또는 '취소' 처리
         log.error("결제 실패: {}", paymentsResponseDTO.failedPayment().paymentFailure().reason());

       }
       case "CANCELLED" -> {
         // 이미 완료된 결제가 취소된 경우 (환불 등)
         // 환불 테이블에 기록하고 주문 상태를 '취소'로 변경
       }
       default -> {
         log.warn("예상치 못한 결제 상태 유입 : {}", paymentsResponseDTO.status());
       }
     }
    return null;
  }

}

