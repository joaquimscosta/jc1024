package com.costacodecraft.toolrental.application.domain.model;

import java.util.UUID;

/**
 * Represents an order with an ID and a rental agreement.
 *
 * @param id the unique identifier of the order
 * @param rentalAgreement the rental agreement associated with the order
 */
public record Order(
    OrderId id,
    RentalAgreement rentalAgreement
) {


  public record OrderId(UUID value) {

    /**
     * Generates a new OrderId with a random UUID.
     *
     * @return a new OrderId with a randomly generated UUID
     */
    public static OrderId autoGenerate() {
      return new OrderId(UUID.randomUUID());
    }
  }
}
