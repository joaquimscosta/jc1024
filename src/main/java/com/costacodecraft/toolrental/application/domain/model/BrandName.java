package com.costacodecraft.toolrental.application.domain.model;

/**
 * Enum representing different brand names.
 */
public enum BrandName {
  STIHL("Stihl"),
  WERNER("Werner"),
  DEWALT("DeWalt"),
  REDGID("Ridgid");

  private final String name;

  BrandName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
