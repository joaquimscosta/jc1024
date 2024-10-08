package com.costacodecraft.toolrental.adapters.out.persistence;

import com.costacodecraft.toolrental.application.domain.model.Order;
import com.costacodecraft.toolrental.application.domain.model.Order.OrderId;
import com.costacodecraft.toolrental.application.port.out.OrderRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * In-memory implementation of the OrderRepository.
 */
@Repository
public class InMemoryOrderRepository implements OrderRepository {

  private final Map<OrderId, Order> ordersDB = new HashMap<>();

  @Override
  public void createOrder(Order order) {
    ordersDB.put(order.id(), order);
  }

  @Override
  public Optional<Order> findById(OrderId id) {
    return Optional.ofNullable(ordersDB.get(id));
  }
}
