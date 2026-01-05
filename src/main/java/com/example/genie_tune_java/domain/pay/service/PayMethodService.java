package com.example.genie_tune_java.domain.pay.service;

import com.example.genie_tune_java.domain.pay.dto.payment_method.PayMethodRegisterInputDTO;
import com.example.genie_tune_java.domain.pay.entity.PayMethod;

public interface PayMethodService {
  PayMethod registerPayMethod(PayMethodRegisterInputDTO dto);
}
