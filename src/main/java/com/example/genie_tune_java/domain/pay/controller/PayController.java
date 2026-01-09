package com.example.genie_tune_java.domain.pay.controller;

import com.example.genie_tune_java.application.payment.PaymentConfirmFacade;
import com.example.genie_tune_java.domain.pay.dto.PaymentCheckRequestDTO;
import com.example.genie_tune_java.domain.pay.dto.PaymentCheckResponseDTO;
import com.example.genie_tune_java.domain.pay.dto.pageable.GetPaymentRequestDTO;
import com.example.genie_tune_java.domain.pay.dto.pageable.MyPaymentPageResponse;
import com.example.genie_tune_java.domain.pay.service.PayService;
import com.example.genie_tune_java.security.util.authorize.IsMemberUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@Log4j2
@RequiredArgsConstructor
public class PayController {
  private final PaymentConfirmFacade paymentConfirmFacade;
  private final PayService payService;

  @MutationMapping
  @IsMemberUser
  public PaymentCheckResponseDTO checkPayment(@Argument("input") PaymentCheckRequestDTO dto) {
    return paymentConfirmFacade.checkPayment(dto);
  }

  @QueryMapping
  @IsMemberUser
  public MyPaymentPageResponse getPaymentHistory(@Argument("input") GetPaymentRequestDTO dto) {
    return payService.findIndividualAll(dto.page(), dto.size());
  }
}
