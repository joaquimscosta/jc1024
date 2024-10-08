package com.costacodecraft.toolrental.application.port.in;

import com.costacodecraft.toolrental.application.domain.model.Tool;

/**
 * Use case for retrieving tools from the inventory.r
 */
public interface GetToolUseCase {

  /**
   * Retrieves all tools from the inventory.
   *
   * @return an iterable collection of all tools
   */
  Iterable<Tool> getAllTools();
}
