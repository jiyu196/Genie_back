package com.example.genie_tune_java.domain.order.service;

import com.example.genie_tune_java.domain.order.dto.MakeOrderRequestDTO;
import com.example.genie_tune_java.domain.order.dto.MakeOrderResponseDTO;
import com.example.genie_tune_java.domain.order.entity.Order;
import com.example.genie_tune_java.domain.order.entity.OrderStatus;

import java.util.Optional;

public interface OrderService {
  MakeOrderResponseDTO makeOrder(MakeOrderRequestDTO dto);
  Order updateOrderStatus(String orderUuid, OrderStatus orderStatus);
  Optional<Order> checkOrder(MakeOrderRequestDTO dto);
}
