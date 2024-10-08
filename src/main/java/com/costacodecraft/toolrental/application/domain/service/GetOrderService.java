package com.costacodecraft.toolrental.application.domain.service;

import com.costacodecraft.toolrental.application.domain.model.Order;
import com.costacodecraft.toolrental.application.domain.model.Order.OrderId;
import com.costacodecraft.toolrental.application.port.in.GetOrderUseCase;
import com.costacodecraft.toolrental.application.port.out.OrderRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Service for retrieving orders by their ID.
 */
@Service
public class GetOrderService implements GetOrderUseCase {

  private final OrderRepository orderRepository;

  public GetOrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public Optional<Order> getOrder(OrderId id) {
    return orderRepository.findById(id);
  }
}
