package com.costacodecraft.toolrental.application.port.in;

import com.costacodecraft.toolrental.application.domain.model.Tool;
import java.util.List;

/**
 * Use case for adding tools to the inventory.
 */
public interface AddToolUseCase {

  /**
   * Adds a single tool to the inventory.
   *
   * @param tool the tool to add
   */
  void addTool(Tool tool);

  /**
   * Adds multiple tools to the inventory.
   *
   * @param tools the list of tools to add
   */
  void addTools(List<Tool> tools);
}
