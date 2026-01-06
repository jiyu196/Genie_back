package com.example.genie_tune_java.domain.pay.controller;

import com.example.genie_tune_java.application.PaymentConfirmFacade;
import com.example.genie_tune_java.domain.pay.dto.PaymentCheckRequestDTO;
import com.example.genie_tune_java.domain.pay.dto.PaymentCheckResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@Log4j2
@RequiredArgsConstructor
public class PayController {
  private final PaymentConfirmFacade paymentConfirmFacade;

  @MutationMapping
  public PaymentCheckResponseDTO checkPayment(@Argument("input") PaymentCheckRequestDTO dto) {
    return paymentConfirmFacade.checkPayment(dto);
  }
}
