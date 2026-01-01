package com.example.genie_tune_java.domain.order.service;

import com.example.genie_tune_java.domain.order.dto.MakeOrderRequestDTO;
import com.example.genie_tune_java.domain.order.dto.MakeOrderResponseDTO;

public interface OrderService {
  MakeOrderResponseDTO makeOrder(MakeOrderRequestDTO dto);
}
