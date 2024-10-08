package com.costacodecraft.toolrental.adapters.out.persistence;

import com.costacodecraft.toolrental.application.domain.model.BrandName;
import com.costacodecraft.toolrental.application.domain.model.Price;
import com.costacodecraft.toolrental.application.domain.model.Tool;
import com.costacodecraft.toolrental.application.domain.model.Tool.ToolId;
import com.costacodecraft.toolrental.application.domain.model.ToolType;
import com.costacodecraft.toolrental.application.domain.service.AddToolService;
import com.costacodecraft.toolrental.application.port.in.AddToolUseCase;
import java.util.List;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Component responsible for loading initial data into the application.
 */
@Component
public class DataLoader {

  private final AddToolUseCase addToolUseCase;

  public DataLoader(AddToolService addToolUseCase) {
    this.addToolUseCase = addToolUseCase;
  }

  @EventListener(ApplicationStartedEvent.class)
  public void loadData() {
    var tools = List.of(
        new Tool(new ToolId("CHNS"), ToolType.CHAINSAW, BrandName.STIHL,
            Price.newBuilder()
                .dailyCharge(1.49)
                .weekdayCharge(true)
                .weekendCharge(false)
                .holidayCharge(true)
                .build()),
        new Tool(new ToolId("LADW"), ToolType.LADDER, BrandName.WERNER,
            Price.newBuilder()
                .dailyCharge(1.99)
                .weekdayCharge(true)
                .weekendCharge(true)
                .holidayCharge(false)
                .build()),
        new Tool(new ToolId("JAKD"), ToolType.JACKHAMMER, BrandName.DEWALT,
            Price.newBuilder()
                .dailyCharge(2.99)
                .weekdayCharge(true)
                .weekendCharge(false)
                .holidayCharge(false)
                .build()),
        new Tool(new ToolId("JAKR"), ToolType.JACKHAMMER, BrandName.REDGID,
            Price.newBuilder()
                .dailyCharge(2.99)
                .weekdayCharge(true)
                .weekendCharge(false)
                .holidayCharge(false)
                .build())

    );
    addToolUseCase.addTools(tools);
  }

}
