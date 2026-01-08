package com.example.genie_tune_java.domain.pay.service;

import com.example.genie_tune_java.domain.pay.dto.pageable.MyPaymentPageResponse;
import com.example.genie_tune_java.domain.pay.dto.success.PaySuccessRegisterInputDTO;
import com.example.genie_tune_java.domain.pay.dto.success.PaySuccessRegisterOutputDTO;

public interface PayService {
  PaySuccessRegisterOutputDTO paySuccessRegister(PaySuccessRegisterInputDTO dto);
  MyPaymentPageResponse findIndividualAll(int page, int size);
}
