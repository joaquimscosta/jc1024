package com.costacodecraft.toolrental.application.domain.exception;

/**
 * Exception thrown when an invalid discount is entered.
 */
public class InvalidDiscountException extends RuntimeException {

  public InvalidDiscountException(String message) {
    super(message);
  }
}
