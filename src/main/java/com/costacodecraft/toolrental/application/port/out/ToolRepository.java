package com.costacodecraft.toolrental.application.port.out;

import com.costacodecraft.toolrental.application.domain.model.Tool;
import com.costacodecraft.toolrental.application.domain.model.Tool.ToolId;
import java.util.Optional;

/**
 * Repository interface for managing tools.
 */
public interface ToolRepository {

  /**
   * Adds a new tool to the repository.
   *
   * @param tool the tool to be added
   */
  void addTool(Tool tool);

  /**
   * Finds all tools in the repository.
   *
   * @return an Iterable containing all tools
   */
  Iterable<Tool> findAll();

  /**
   * Finds a tool by its ID.
   *
   * @param id the ID of the tool to be found
   * @return an Optional containing the tool if found, or empty if not found
   */
  Optional<Tool> findById(ToolId id);
}
