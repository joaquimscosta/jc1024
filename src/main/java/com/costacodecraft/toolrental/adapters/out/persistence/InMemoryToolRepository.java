package com.costacodecraft.toolrental.adapters.out.persistence;

import com.costacodecraft.toolrental.application.domain.model.Tool;
import com.costacodecraft.toolrental.application.domain.model.Tool.ToolId;
import com.costacodecraft.toolrental.application.port.out.ToolRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * In-memory implementation of the ToolRepository.
 */
@Repository
public class InMemoryToolRepository implements ToolRepository {

  private final Map<ToolId, Tool> toolsDB = new HashMap<>();

  @Override
  public void addTool(Tool tool) {
    toolsDB.put(tool.id(), tool);
  }

  @Override
  public Iterable<Tool> findAll() {
    return toolsDB.values();
  }

  @Override
  public Optional<Tool> findById(ToolId id) {
    return Optional.ofNullable(toolsDB.get(id));
  }
}
