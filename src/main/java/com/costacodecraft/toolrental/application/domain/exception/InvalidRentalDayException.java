package com.costacodecraft.toolrental.application.domain.exception;

/**
 * Exception thrown when an invalid rental day is entered.
 */
public class InvalidRentalDayException extends RuntimeException {

  public InvalidRentalDayException(String message) {
    super(message);
  }

}
