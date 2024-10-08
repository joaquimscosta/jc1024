package com.costacodecraft.toolrental.application.domain.model;

/**
 * Enum representing different types of tools.
 */
public enum ToolType {
  CHAINSAW("Chainsaw"),
  LADDER("Ladder"),
  JACKHAMMER("Jackhammer");
  private final String type;

  ToolType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
