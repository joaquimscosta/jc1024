package com.costacodecraft.toolrental.application.domain.exception;

/**
 * Exception thrown when invalid data is entered.
 */
public class InvalidDataEntryException extends RuntimeException {

  public InvalidDataEntryException(String message) {
    super(message);
  }
}
