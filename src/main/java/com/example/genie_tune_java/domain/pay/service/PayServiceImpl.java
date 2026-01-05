package com.example.genie_tune_java.domain.pay.service;

import com.example.genie_tune_java.domain.pay.dto.success.PaySuccessRegisterInputDTO;
import com.example.genie_tune_java.domain.pay.dto.success.PaySuccessRegisterOutputDTO;
import com.example.genie_tune_java.domain.pay.entity.Pay;
import com.example.genie_tune_java.domain.pay.mapper.PayMapper;
import com.example.genie_tune_java.domain.pay.repository.PayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class PayServiceImpl implements PayService {
  private final PayRepository payRepository;
  private final PayMapper payMapper;

  @Override
  @Transactional
  public PaySuccessRegisterOutputDTO paySuccessRegister(PaySuccessRegisterInputDTO dto) {
    Pay pay = payMapper.toSuccessEntity(dto);
    payRepository.save(pay);
    return payMapper.toSuccessDto(pay);
  }
}
