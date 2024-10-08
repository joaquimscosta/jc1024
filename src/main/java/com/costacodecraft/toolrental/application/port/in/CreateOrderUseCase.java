package com.costacodecraft.toolrental.application.port.in;

import com.costacodecraft.toolrental.application.domain.model.Order;
import com.costacodecraft.toolrental.application.domain.model.RentalAgreement;

/**
 * Use case for creating orders based on rental agreements.
 */
public interface CreateOrderUseCase {

  /**
   * Creates an order from the specified rental agreement.
   *
   * @param rentalAgreement the rental agreement
   * @return the created order
   */
  Order createOrder(RentalAgreement rentalAgreement);
}
