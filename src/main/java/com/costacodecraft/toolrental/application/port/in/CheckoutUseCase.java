package com.costacodecraft.toolrental.application.port.in;

import com.costacodecraft.toolrental.application.domain.model.Order.OrderId;
import java.util.List;

/**
 * Use case for processing the checkout of tools.
 */
public interface CheckoutUseCase {

  /**
   * Processes the checkout of a list of items.
   *
   * @param checkoutCommands the list of items to checkout
   * @return a list of OrderIds representing the created orders
   */
  List<OrderId> checkout(List<CheckoutCommand> checkoutCommands);
}