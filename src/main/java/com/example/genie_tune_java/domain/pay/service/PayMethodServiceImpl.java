package com.example.genie_tune_java.domain.pay.service;

import com.example.genie_tune_java.domain.pay.dto.payment_method.PayMethodRegisterInputDTO;
import com.example.genie_tune_java.domain.pay.entity.PayMethod;
import com.example.genie_tune_java.domain.pay.mapper.PayMethodMapper;
import com.example.genie_tune_java.domain.pay.repository.PayMethodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class PayMethodServiceImpl implements PayMethodService {
  private final PayMethodRepository payMethodRepository;
  private final PayMethodMapper payMethodMapper;

  @Override
  public PayMethod registerPayMethod(PayMethodRegisterInputDTO dto) {

    PayMethod payMethod = payMethodMapper.toSuccessEntity(dto);
    payMethodRepository.save(payMethod);

    return payMethod;
  }
}
