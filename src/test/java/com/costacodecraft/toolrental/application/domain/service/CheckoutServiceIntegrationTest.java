package com.costacodecraft.toolrental.application.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.costacodecraft.toolrental.adapters.out.persistence.InMemoryOrderRepository;
import com.costacodecraft.toolrental.adapters.out.persistence.InMemoryToolRepository;
import com.costacodecraft.toolrental.application.domain.exception.InvalidDiscountException;
import com.costacodecraft.toolrental.application.domain.model.BrandName;
import com.costacodecraft.toolrental.application.domain.model.Order;
import com.costacodecraft.toolrental.application.domain.model.Order.OrderId;
import com.costacodecraft.toolrental.application.domain.model.Price;
import com.costacodecraft.toolrental.application.domain.model.Tool;
import com.costacodecraft.toolrental.application.domain.model.Tool.ToolId;
import com.costacodecraft.toolrental.application.domain.model.ToolType;
import com.costacodecraft.toolrental.application.port.in.CheckoutCommand;
import com.costacodecraft.toolrental.application.port.in.CheckoutUseCase;
import com.costacodecraft.toolrental.application.port.in.CreateOrderUseCase;
import com.costacodecraft.toolrental.application.port.in.GeHolidayUseCase;
import com.costacodecraft.toolrental.application.port.in.GetChargeableDaysUseCase;
import com.costacodecraft.toolrental.application.port.out.OrderRepository;
import com.costacodecraft.toolrental.application.port.out.ToolRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Integration tests for the CheckoutService.
 * These test cases assume that the tools are configured as specified in {@link #getTools()}.
 */
class CheckoutServiceIntegrationTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(
      CheckoutServiceIntegrationTest.class);

  private ToolRepository toolRepository;
  private OrderRepository orderRepository;
  private CheckoutUseCase checkoutUseCase;

  /**
   * Sets up the test environment before each test.
   */
  @BeforeEach
  void setUp() {
    toolRepository = new InMemoryToolRepository();
    orderRepository = new InMemoryOrderRepository();
    CreateOrderUseCase createOrderUseCase = new CreateOrderService(orderRepository);
    GeHolidayUseCase geHolidayUseCase = new GetHolidayService();
    GetChargeableDaysUseCase getChargeableDaysUseCase = new GetChargeableDaysService(
        geHolidayUseCase);
    CheckoutValidator checkoutValidator = new CheckoutValidator();
    checkoutUseCase = new CheckoutService(toolRepository, createOrderUseCase,
        getChargeableDaysUseCase, checkoutValidator);

    // load test data
    loadTools();
  }

  /**
   * Tests that an InvalidDiscountException is thrown when the discount percentage is invalid.
   */
  @Test
  void test1_InvalidDiscount() {
    var checkoutCommand = new CheckoutCommand(
        "JAKR",
        5,
        101,
        LocalDate.of(2015, Month.SEPTEMBER, 3)
    );

    assertThatExceptionOfType(InvalidDiscountException.class)
        .isThrownBy(() -> checkoutUseCase.checkout(List.of(checkoutCommand)))
        .withMessage("Discount percent must be between 0 and 100");
  }

  /**
   * Tests that no holiday charge is applied when Independence Day is observed on a Friday.
   */
  @Test
  void test2_IndependenceDayObservedFriday_NoHolidayCharge() {
    var toolCode = "LADW";
    var checkoutDate = LocalDate.of(2020, Month.JULY, 2);
    var checkoutCommand = new CheckoutCommand(
        toolCode,
        3,
        10,
        checkoutDate
    );

    var tool = getTools().get(toolCode);

    List<OrderId> result = checkoutUseCase.checkout(List.of(checkoutCommand));
    assertThat(result).isNotNull();
    assertThat(result).hasSize(1);

    Optional<Order> order = orderRepository.findById(result.getFirst());
    assertThat(order).isPresent()
        .hasValueSatisfying(value -> {
          LOGGER.info("Rental Agreement: {}", order.get().rentalAgreement().getPrettyPrintText());
          var rentalAgreement = value.rentalAgreement();
          assertThat(rentalAgreement).isNotNull();
          assertThat(rentalAgreement.rentalDays()).isEqualTo(checkoutCommand.rentalDays());
          assertThat(rentalAgreement.checkoutDate()).isEqualTo(checkoutCommand.checkoutDate());
          assertThat(rentalAgreement.discountPercent()).isEqualTo(
              checkoutCommand.discountPercentage());
          assertThat(rentalAgreement.toolCode()).isEqualTo(checkoutCommand.toolCode());
          assertThat(rentalAgreement.returnDate()).isEqualTo(
              checkoutCommand.checkoutDate().plusDays(checkoutCommand.rentalDays()));
          assertThat(rentalAgreement.chargeDays()).isEqualTo(2);
          assertThat(rentalAgreement.dailyChargeAmount()).isEqualTo(tool.price().dailyCharge());
          assertThat(rentalAgreement.toolCode()).isEqualTo(tool.id().code());

          var preDiscountAmount = calculatePreDiscount(2, tool.price().dailyCharge());
          assertThat(rentalAgreement.preDiscountAmount()).isEqualTo(preDiscountAmount);

          var discount = calculateDiscount(checkoutCommand.discountPercentage(), preDiscountAmount);
          assertThat(rentalAgreement.discountAmount()).isEqualTo(discount);

          var finalCharge = preDiscountAmount.subtract(discount);

          assertThat(rentalAgreement.finalChargeAmount()).isEqualTo(finalCharge);
        });
  }

  /**
   * Tests that holiday charge is applied but no weekend charge when Independence Day is observed on a Friday.
   */
  @Test
  void test3_IndependenceDayObservedFriday_YesHolidayChargeNoWeekendCharge() {
    var toolCode = "CHNS";
    var checkoutDate = LocalDate.of(2015, Month.JULY, 2);
    var checkoutCommand = new CheckoutCommand(
        toolCode,
        5,
        25,
        checkoutDate
    );
    var tool = getTools().get(toolCode);

    List<OrderId> result = checkoutUseCase.checkout(List.of(checkoutCommand));
    assertThat(result).isNotNull();
    assertThat(result).hasSize(1);

    Optional<Order> order = orderRepository.findById(result.getFirst());
    assertThat(order).isPresent()
        .hasValueSatisfying(value -> {
          LOGGER.info("Rental Agreement: {}", order.get().rentalAgreement().getPrettyPrintText());
          var rentalAgreement = value.rentalAgreement();
          assertThat(rentalAgreement).isNotNull();
          assertThat(rentalAgreement.rentalDays()).isEqualTo(checkoutCommand.rentalDays());
          assertThat(rentalAgreement.checkoutDate()).isEqualTo(checkoutCommand.checkoutDate());
          assertThat(rentalAgreement.discountPercent()).isEqualTo(
              checkoutCommand.discountPercentage());
          assertThat(rentalAgreement.toolCode()).isEqualTo(checkoutCommand.toolCode());
          assertThat(rentalAgreement.returnDate()).isEqualTo(
              checkoutCommand.checkoutDate().plusDays(checkoutCommand.rentalDays()));
          assertThat(rentalAgreement.chargeDays()).isEqualTo(3);
          assertThat(rentalAgreement.dailyChargeAmount()).isEqualTo(tool.price().dailyCharge());
          assertThat(rentalAgreement.toolCode()).isEqualTo(tool.id().code());

          var preDiscountAmount = calculatePreDiscount(3, tool.price().dailyCharge());
          assertThat(rentalAgreement.preDiscountAmount()).isEqualTo(preDiscountAmount);

          var discount = calculateDiscount(checkoutCommand.discountPercentage(), preDiscountAmount);
          assertThat(rentalAgreement.discountAmount()).isEqualTo(discount);

          var finalCharge = preDiscountAmount.subtract(discount);

          assertThat(rentalAgreement.finalChargeAmount()).isEqualTo(finalCharge);
        });
  }

  /**
   * Tests that no weekend charge is applied.
   */
  @Test
  void test4_NoWeekendCharge() {
    var toolCode = "JAKD";
    var checkoutDate = LocalDate.of(2015, Month.SEPTEMBER, 3);
    var checkoutCommand = new CheckoutCommand(
        toolCode,
        6,
        0,
        checkoutDate
    );
    var tool = getTools().get(toolCode);

    List<OrderId> result = checkoutUseCase.checkout(List.of(checkoutCommand));
    assertThat(result).isNotNull();
    assertThat(result).hasSize(1);

    Optional<Order> order = orderRepository.findById(result.getFirst());
    assertThat(order).isPresent()
        .hasValueSatisfying(value -> {
          LOGGER.info("Rental Agreement: {}", order.get().rentalAgreement().getPrettyPrintText());
          var rentalAgreement = value.rentalAgreement();
          assertThat(rentalAgreement).isNotNull();
          assertThat(rentalAgreement.rentalDays()).isEqualTo(checkoutCommand.rentalDays());
          assertThat(rentalAgreement.checkoutDate()).isEqualTo(checkoutCommand.checkoutDate());
          assertThat(rentalAgreement.discountPercent()).isEqualTo(
              checkoutCommand.discountPercentage());
          assertThat(rentalAgreement.toolCode()).isEqualTo(checkoutCommand.toolCode());
          assertThat(rentalAgreement.returnDate()).isEqualTo(
              checkoutCommand.checkoutDate().plusDays(checkoutCommand.rentalDays()));
          assertThat(rentalAgreement.chargeDays()).isEqualTo(3);
          assertThat(rentalAgreement.dailyChargeAmount()).isEqualTo(tool.price().dailyCharge());
          assertThat(rentalAgreement.toolCode()).isEqualTo(tool.id().code());

          var preDiscountAmount = calculatePreDiscount(3, tool.price().dailyCharge());
          assertThat(rentalAgreement.preDiscountAmount()).isEqualTo(preDiscountAmount);

          var discount = calculateDiscount(checkoutCommand.discountPercentage(), preDiscountAmount);
          assertThat(rentalAgreement.discountAmount()).isEqualTo(discount);

          var finalCharge = preDiscountAmount.subtract(discount);

          assertThat(rentalAgreement.finalChargeAmount()).isEqualTo(finalCharge);
        });
  }

  /**
   * Tests that no holiday charge and no weekend charge are applied.
   */
  @Test
  void test5_NoHolidayChargeAndNoWeekendCharge() {
    var toolCode = "JAKR";
    var checkoutDate = LocalDate.of(2015, Month.JULY, 2);
    var checkoutCommand = new CheckoutCommand(
        toolCode,
        9,
        0,
        checkoutDate
    );
    var tool = getTools().get(toolCode);

    List<OrderId> result = checkoutUseCase.checkout(List.of(checkoutCommand));
    assertThat(result).isNotNull();
    assertThat(result).hasSize(1);

    Optional<Order> order = orderRepository.findById(result.getFirst());
    assertThat(order).isPresent()
        .hasValueSatisfying(value -> {
          LOGGER.info("Rental Agreement: {}", order.get().rentalAgreement().getPrettyPrintText());
          var rentalAgreement = value.rentalAgreement();
          assertThat(rentalAgreement).isNotNull();
          assertThat(rentalAgreement.rentalDays()).isEqualTo(checkoutCommand.rentalDays());
          assertThat(rentalAgreement.checkoutDate()).isEqualTo(checkoutCommand.checkoutDate());
          assertThat(rentalAgreement.discountPercent()).isEqualTo(
              checkoutCommand.discountPercentage());
          assertThat(rentalAgreement.toolCode()).isEqualTo(checkoutCommand.toolCode());
          assertThat(rentalAgreement.returnDate()).isEqualTo(
              checkoutCommand.checkoutDate().plusDays(checkoutCommand.rentalDays()));
          assertThat(rentalAgreement.chargeDays()).isEqualTo(5);
          assertThat(rentalAgreement.dailyChargeAmount()).isEqualTo(tool.price().dailyCharge());
          assertThat(rentalAgreement.toolCode()).isEqualTo(tool.id().code());

          var preDiscountAmount = calculatePreDiscount(5, tool.price().dailyCharge());
          assertThat(rentalAgreement.preDiscountAmount()).isEqualTo(preDiscountAmount);

          var discount = calculateDiscount(checkoutCommand.discountPercentage(), preDiscountAmount);
          assertThat(rentalAgreement.discountAmount()).isEqualTo(discount);

          var finalCharge = preDiscountAmount.subtract(discount);

          assertThat(rentalAgreement.finalChargeAmount()).isEqualTo(finalCharge);
        });
  }

  /**
   * Tests that no holiday charge and no weekend charge are applied.
   */
  @Test
  void test6_NoHolidayChargeAndNoWeekendCharge() {
    var toolCode = "JAKR";
    var checkoutDate = LocalDate.of(2020, Month.JULY, 2);
    var checkoutCommand = new CheckoutCommand(
        toolCode,
        4,
        50,
        checkoutDate
    );
    var tool = getTools().get(toolCode);

    List<OrderId> result = checkoutUseCase.checkout(List.of(checkoutCommand));
    assertThat(result).isNotNull();
    assertThat(result).hasSize(1);

    Optional<Order> order = orderRepository.findById(result.getFirst());
    assertThat(order).isPresent()
        .hasValueSatisfying(value -> {
          LOGGER.info("Rental Agreement: {}", order.get().rentalAgreement().getPrettyPrintText());
          var rentalAgreement = value.rentalAgreement();
          assertThat(rentalAgreement).isNotNull();
          assertThat(rentalAgreement.rentalDays()).isEqualTo(checkoutCommand.rentalDays());
          assertThat(rentalAgreement.checkoutDate()).isEqualTo(checkoutCommand.checkoutDate());
          assertThat(rentalAgreement.discountPercent()).isEqualTo(
              checkoutCommand.discountPercentage());
          assertThat(rentalAgreement.toolCode()).isEqualTo(checkoutCommand.toolCode());
          assertThat(rentalAgreement.returnDate()).isEqualTo(
              checkoutCommand.checkoutDate().plusDays(checkoutCommand.rentalDays()));
          assertThat(rentalAgreement.chargeDays()).isEqualTo(1);
          assertThat(rentalAgreement.dailyChargeAmount()).isEqualTo(tool.price().dailyCharge());
          assertThat(rentalAgreement.toolCode()).isEqualTo(tool.id().code());

          var preDiscountAmount = calculatePreDiscount(1, tool.price().dailyCharge());
          assertThat(rentalAgreement.preDiscountAmount()).isEqualTo(preDiscountAmount);

          var discount = calculateDiscount(checkoutCommand.discountPercentage(), preDiscountAmount);
          assertThat(rentalAgreement.discountAmount()).isEqualTo(discount);

          var finalCharge = preDiscountAmount.subtract(discount);

          assertThat(rentalAgreement.finalChargeAmount()).isEqualTo(finalCharge);
        });
  }


  /**
   * Loads the tool inventory with the test data.
   */
  private void loadTools() {
    var tools = getTools().values();
    tools.forEach(toolRepository::addTool);
    LOGGER.info("Loaded {} tools", tools.size());
  }


  private static BigDecimal calculateDiscount(int discount, BigDecimal amount) {
    return amount.multiply(BigDecimal.valueOf(discount / 100.0)).setScale(2, RoundingMode.HALF_UP);
  }

  private static BigDecimal calculatePreDiscount(int days, double dailyCharge) {
    return BigDecimal.valueOf(days * dailyCharge).setScale(2, RoundingMode.HALF_UP);
  }

  /**
   * Loads the tool inventory with the same data provided in the specs document.
   *
   * @return a map of tool codes to tools
   */
  private static Map<String, Tool> getTools() {
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
    return tools.stream()
        .collect(Collectors.toMap(
            (tool) -> tool.id().code(),
            Function.identity()
        ));
  }
}