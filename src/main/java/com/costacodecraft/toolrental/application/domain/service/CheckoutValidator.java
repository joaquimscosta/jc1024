package com.costacodecraft.toolrental.application.domain.service;

import com.costacodecraft.toolrental.application.domain.exception.InvalidDiscountException;
import com.costacodecraft.toolrental.application.domain.exception.InvalidRentalDayException;
import com.costacodecraft.toolrental.application.port.in.CheckoutCommand;
import org.springframework.stereotype.Component;


/**
 * Validator for checkout items.
 */
@Component
public class CheckoutValidator {

  /**
   * Validates a checkout item.
   *
   * @param checkoutCommand the item to validate
   * @throws InvalidRentalDayException if rental days are less than 1
   * @throws InvalidDiscountException if discount percentage is not between 0 and 100
   */
  public void validate(CheckoutCommand checkoutCommand) {
    // It would also, be good to set a max rental days allowed!
    if (checkoutCommand.rentalDays() < 1) {
      throw new InvalidRentalDayException("Rental days must be 1 or more days");
    }
    if (checkoutCommand.discountPercentage() < 0 || checkoutCommand.discountPercentage() > 100) {
      throw new InvalidDiscountException("Discount percent must be between 0 and 100");
    }
  }
}
