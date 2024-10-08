package com.costacodecraft.toolrental.application.domain.service;

import com.costacodecraft.toolrental.application.domain.model.Tool;
import com.costacodecraft.toolrental.application.port.in.GetToolUseCase;
import com.costacodecraft.toolrental.application.port.out.ToolRepository;
import org.springframework.stereotype.Service;


/**
 * Service for retrieving tools.
 */
@Service
public class GetToolService implements GetToolUseCase {

  private final ToolRepository toolRepository;

  public GetToolService(ToolRepository toolRepository) {
    this.toolRepository = toolRepository;

  }

  @Override
  public Iterable<Tool> getAllTools() {
    return toolRepository.findAll();
  }
}
