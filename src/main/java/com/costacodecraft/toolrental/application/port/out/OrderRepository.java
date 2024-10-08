package com.costacodecraft.toolrental.application.port.out;

import com.costacodecraft.toolrental.application.domain.model.Order;
import com.costacodecraft.toolrental.application.domain.model.Order.OrderId;
import java.util.Optional;

/**
 * Repository interface for managing orders.
 */
public interface OrderRepository {

  /**
   * Creates a new order.
   *
   * @param order the order to be created
   */
  void createOrder(Order order);

  /**
   * Finds an order by its ID.
   *
   * @param id the ID of the order to be found
   * @return an Optional containing the order if found, or empty if not found
   */
  Optional<Order> findById(OrderId id);
}
