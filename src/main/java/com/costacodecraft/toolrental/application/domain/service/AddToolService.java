package com.costacodecraft.toolrental.application.domain.service;

import com.costacodecraft.toolrental.application.domain.model.Tool;
import com.costacodecraft.toolrental.application.port.in.AddToolUseCase;
import com.costacodecraft.toolrental.application.port.out.ToolRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service implementation for adding tools.
 */
@Service
public class AddToolService implements AddToolUseCase {

  private final ToolRepository toolRepository;

  public AddToolService(ToolRepository toolRepository) {
    this.toolRepository = toolRepository;
  }

  @Override
  public void addTool(Tool tool) {
    toolRepository.addTool(tool);
  }

  @Override
  public void addTools(List<Tool> tools) {
    tools.forEach(this::addTool);
  }
}
