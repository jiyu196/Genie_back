package com.example.genie_tune_java.application;

import com.example.genie_tune_java.api.port_one.dto.PortOneGetPaymentsRequestDTO;
import com.example.genie_tune_java.api.port_one.dto.PortOneGetPaymentsResponseDTO;
import com.example.genie_tune_java.api.port_one.service.PortOneService;
import com.example.genie_tune_java.domain.pay.dto.PaymentCheckRequestDTO;
import com.example.genie_tune_java.domain.pay.dto.PaymentCheckResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentConfirmFacade {
  private final PortOneService portOneService;
  private final PaymentStatusHandler paymentStatusHandler;
  @Value("${PORTONE_STORE_ID}")
  private String storeId;

  public PaymentCheckResponseDTO checkPayment(PaymentCheckRequestDTO dto) {
    //1. 프론트에서 보내준 DTO 값으로 PortOne의 결제정보를 가져올 때 필요한 Record Type의 DTO로 변환
    PortOneGetPaymentsRequestDTO paymentsRequestDTO = new PortOneGetPaymentsRequestDTO(dto.paymentId(), storeId);
    //2. PortOne 결제 조회 서비스 호출(단일 결제 건) -> 얘는 Transactional 있으면 안됨.
    PortOneGetPaymentsResponseDTO paymentsResponseDTO = portOneService.getPayments(paymentsRequestDTO);
    log.info("payments: {} {}", paymentsResponseDTO.id(), paymentsResponseDTO.transactionId());
    String status = paymentsResponseDTO.status();
    log.info("결제 상태: {}", status);
    //4. 결제 조회 내용으로 Table Mapping 별도 클래스 파일 메서드로 빼서 여기만 Transactional 부여
    return paymentStatusHandler.handlePayment(status, dto, paymentsResponseDTO);
  }

}

