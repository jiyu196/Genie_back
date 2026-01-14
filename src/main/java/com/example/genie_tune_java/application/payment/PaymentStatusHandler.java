package com.example.genie_tune_java.application.payment;

import com.example.genie_tune_java.api.port_one.dto.PortOneGetPaymentsResponseDTO;
import com.example.genie_tune_java.domain.member.entity.Role;
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
import com.example.genie_tune_java.domain.service_access.dto.ServiceAccessRegisterInputDTO;
import com.example.genie_tune_java.domain.service_access.service.ServiceAccessService;
import com.example.genie_tune_java.domain.subscription.dto.SubscriptionRegisterInputDTO;
import com.example.genie_tune_java.domain.subscription.entity.Subscription;
import com.example.genie_tune_java.domain.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentStatusHandler {

  private final OrderService orderService;
  private final PayService payService;
  private final PayMethodService payMethodService;
  private final SubscriptionService subscriptionService;
  private final ServiceAccessService serviceAccessService;

  @Transactional
  public PaymentCheckResponseDTO handlePayment(String status, PaymentCheckRequestDTO dto, PortOneGetPaymentsResponseDTO paymentsResponseDTO) {

    switch(status) {
      case "PAID" -> {
        //OrderService로 기존 Order 객체 변경, PayService 및 PayMethodService를 통해 pay table 등록, pay Method 등록
        //1. OrderService changeStatus 호출
        Order order = orderService.updateOrderStatus(dto.paymentId(), OrderStatus.SUCCESS);
        log.info("주문 멤버: {}, 주문 상품: {}", order.getMember(), order.getProduct().getDisplayName());
        //2. PayMethodService 호출 (성공 Register), 정기결제 위해서 추후 BillingKey 호출하는 것도 필요함.
        PaymentMethod paymentMethod = paymentsResponseDTO.paymentMethod();
        PayMethodRegisterInputDTO payMethodRegisterInputDTO = new PayMethodRegisterInputDTO(order.getMember(), paymentMethod.type(),paymentMethod.card().publisher(),paymentMethod.card().number());
        PayMethod payMethod = payMethodService.registerPayMethod(payMethodRegisterInputDTO); //반환 값 PayMethod -> 아래 SubsCription이 사용함

        //3. PayService 호출 (성공 Register)
        PaySuccessRegisterInputDTO paySuccessRegisterInputDTO = new PaySuccessRegisterInputDTO(order, order.getMember(), PayStatus.PAID,
                paymentsResponseDTO.amount().total() * 1000, paymentsResponseDTO.transactionId(), paymentsResponseDTO.paidAt(), paymentsResponseDTO.pgTxId(),
                paymentsResponseDTO.pgResponse(), paymentsResponseDTO.receiptUrl(), payMethod.getPgType().name(), payMethod.getCardCompany());
        PaySuccessRegisterOutputDTO paySuccessRegisterOutputDTO = payService.paySuccessRegister(paySuccessRegisterInputDTO);
        log.info("결제 상태: {}, 결제시각: {}",paySuccessRegisterOutputDTO.payStatus(), paySuccessRegisterOutputDTO.paidAt());

        //4. member 상태값 구독자로 변경
        order.getMember().changeMemberRole(Role.SUBSCRIBER);

        //5. SubscriptionService 호출
        SubscriptionRegisterInputDTO subscriptionRegisterInputDTO = new SubscriptionRegisterInputDTO(order.getMember(), order, order.getProduct(), payMethod);
        Subscription subscription = subscriptionService.registerSubscription(subscriptionRegisterInputDTO);

        //6. AccessIdService 호출
        ServiceAccessRegisterInputDTO serviceAccessRegisterInputDTO = new ServiceAccessRegisterInputDTO(order.getMember(), subscription);
        serviceAccessService.issueServiceAccess(serviceAccessRegisterInputDTO);

        return new PaymentCheckResponseDTO(order.getOrderUuid(), order.getProduct().getDisplayName(), order.getTotalAmount(), paySuccessRegisterOutputDTO.payStatus().name(), order.getProduct().getMaxServiceAccessIdCount(), "구독이 완료되었습니다.", paySuccessRegisterOutputDTO.paidAt());
      }

      case "PAY_PENDING" -> {
        // 주로 가상계좌(Virtual Account) 결제 시 발생
        // 주문 상태를 '입금 대기'로 변경하고, 입금 기한/계좌번호 정보를 DB에 저장
        return null;

      }
      case "FAILED" -> {
        // 결제 실패 사유(failureReason)를 로그에 남기고 주문을 '실패' 또는 '취소' 처리
        //1. Order 객체 취소 처리 -> 이후 로직 다시 주문으로 해야함
        Order order = orderService.updateOrderStatus(dto.paymentId(), OrderStatus.CANCELED);

        //2. 실패시 내려주는 값으로 매칭해서 pay table 기록
        log.error("결제 실패: {}", paymentsResponseDTO.paymentFailure().reason());
        return null;
      }
      case "CANCELLED" -> {
        // 이미 완료된 결제가 취소된 경우 (환불 등)

        return null;
      }
      default -> {
        log.warn("예상치 못한 결제 상태 유입 : {}", paymentsResponseDTO.status());
        return null;
      }
    }
  }
}
