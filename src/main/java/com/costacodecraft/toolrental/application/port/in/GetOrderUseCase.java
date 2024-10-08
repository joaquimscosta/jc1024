package com.costacodecraft.toolrental.application.port.in;

import com.costacodecraft.toolrental.application.domain.model.Order;
import com.costacodecraft.toolrental.application.domain.model.Order.OrderId;
import java.util.Optional;

/**
 * Use case for retrieving orders by their ID.
 */
public interface GetOrderUseCase {

  /**
   * Retrieves an order by its ID.
   *
   * @param id the ID of the order
   * @return an Optional containing the order if found, or empty if not found
   */
  Optional<Order> getOrder(OrderId id);
}