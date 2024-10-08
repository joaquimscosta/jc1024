package com.costacodecraft.toolrental.application.domain.exception;

/**
 * Exception thrown when a tool is not found.
 */
public class ToolNotFoundException extends RuntimeException {

  public ToolNotFoundException(String message) {
    super(message);
  }
}
