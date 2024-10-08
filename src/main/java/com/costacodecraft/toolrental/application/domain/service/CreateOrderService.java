package com.costacodecraft.toolrental.application.domain.service;

import com.costacodecraft.toolrental.application.domain.model.Order;
import com.costacodecraft.toolrental.application.domain.model.Order.OrderId;
import com.costacodecraft.toolrental.application.domain.model.RentalAgreement;
import com.costacodecraft.toolrental.application.port.in.CreateOrderUseCase;
import com.costacodecraft.toolrental.application.port.out.OrderRepository;
import org.springframework.stereotype.Service;

/**
 * Service implementation for creating orders.
 */
@Service
public class CreateOrderService implements CreateOrderUseCase {

  private final OrderRepository orderRepository;

  public CreateOrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public Order createOrder(RentalAgreement rentalAgreement) {
    var order = new Order(OrderId.autoGenerate(), rentalAgreement);
    orderRepository.createOrder(order);
    return order;
  }
}
