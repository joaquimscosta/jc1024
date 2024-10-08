package com.costacodecraft.toolrental.application.domain.service;

import com.costacodecraft.toolrental.application.domain.exception.ToolNotFoundException;
import com.costacodecraft.toolrental.application.domain.model.CheckoutItem;
import com.costacodecraft.toolrental.application.domain.model.Order;
import com.costacodecraft.toolrental.application.domain.model.Order.OrderId;
import com.costacodecraft.toolrental.application.domain.model.RentalAgreement;
import com.costacodecraft.toolrental.application.domain.model.Tool;
import com.costacodecraft.toolrental.application.domain.model.Tool.ToolId;
import com.costacodecraft.toolrental.application.port.in.ChargeableDaysCount;
import com.costacodecraft.toolrental.application.port.in.CheckoutCommand;
import com.costacodecraft.toolrental.application.port.in.CheckoutUseCase;
import com.costacodecraft.toolrental.application.port.in.CreateOrderUseCase;
import com.costacodecraft.toolrental.application.port.in.GetChargeableDaysUseCase;
import com.costacodecraft.toolrental.application.port.out.ToolRepository;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for handling the checkout process of tools.
 */
@Service
public class CheckoutService implements CheckoutUseCase {

  private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutService.class);

  private final ToolRepository toolRepository;
  private final CreateOrderUseCase createOrderUseCase;
  private final GetChargeableDaysUseCase chargeableDaysUseCase;
  private final CheckoutValidator checkoutValidator;

  public CheckoutService(ToolRepository toolRepository, CreateOrderUseCase createOrderUseCase,
      GetChargeableDaysUseCase chargeableDaysUseCase, CheckoutValidator checkoutValidator) {
    this.toolRepository = toolRepository;
    this.createOrderUseCase = createOrderUseCase;
    this.chargeableDaysUseCase = chargeableDaysUseCase;
    this.checkoutValidator = checkoutValidator;
  }

  @Override
  public List<OrderId> checkout(List<CheckoutCommand> checkoutCommands) {
    LOGGER.debug("Starting checkout process for {} items", checkoutCommands.size());
    return checkoutCommands.stream()
        .map(this::processCheckoutItem)
        .toList();
  }

  /**
   * Processes a single checkout item.
   *
   * @param checkoutCommand the item to process
   * @return the UUID of the created order
   */
  private OrderId processCheckoutItem(CheckoutCommand checkoutCommand) {
    LOGGER.debug("Processing checkout item with tool code: {}", checkoutCommand.toolCode());
    checkoutValidator.validate(checkoutCommand);
    ToolId toolId = new ToolId(checkoutCommand.toolCode());
    Tool tool = toolRepository.findById(toolId)
        .orElseThrow(() -> new ToolNotFoundException(
            "Tool with code \"" + checkoutCommand.toolCode() + "\" not found"));
    LocalDate returnDate = checkoutCommand.checkoutDate().plusDays(checkoutCommand.rentalDays());
    ChargeableDaysCount chargeableDaysCount = chargeableDaysUseCase.getChargeableDays(
        checkoutCommand.checkoutDate(), returnDate);
    CheckoutItem checkoutItem = new CheckoutItem(tool, checkoutCommand, chargeableDaysCount);
    RentalAgreement rentalAgreement = rentalAgreementFactory(checkoutItem);
    Order order = createOrderUseCase.createOrder(rentalAgreement);
    LOGGER.debug("Checkout item processed successfully with order ID: {}", order.id());
    return order.id();
  }

  /**
   * Creates a rental agreement for the specified checkout item.
   *
   * @param checkoutItem the details of the checkout item
   * @return the created rental agreement
   */
  private RentalAgreement rentalAgreementFactory(CheckoutItem checkoutItem) {
    Tool tool = checkoutItem.tool();
    return RentalAgreement.newBuilder()
        .toolCode(tool.id().code())
        .toolType(tool.type())
        .toolBrand(tool.brand())
        .rentalDays(checkoutItem.rentalDays())
        .checkoutDate(checkoutItem.checkoutDate())
        .returnDate(checkoutItem.getReturnDate())
        .dailyChargeAmount(tool.price().dailyCharge())
        .chargeDays(checkoutItem.getChargeableDaysCount())
        .preDiscountAmount(checkoutItem.getPreDiscountAmount())
        .discountPercent(checkoutItem.discountPercentage())
        .discountAmount(checkoutItem.getDiscountAmount())
        .finalChargeAmount(checkoutItem.getFinalChargeAmount())
        .build();
  }
}
