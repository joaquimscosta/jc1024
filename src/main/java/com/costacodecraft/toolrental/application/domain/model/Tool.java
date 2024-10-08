package com.costacodecraft.toolrental.application.domain.model;

/**
 * Represents a tool with an ID, type, brand, and price.
 *
 * @param id the unique identifier of the tool
 * @param type the type of the tool
 * @param brand the brand of the tool
 * @param price the price details of the tool
 */
public record Tool(
    ToolId id,
    ToolType type,
    BrandName brand,
    Price price
) {

  public record ToolId(
      String code
  ) {

  }
}

