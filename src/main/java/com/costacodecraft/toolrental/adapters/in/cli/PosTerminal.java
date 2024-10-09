package com.costacodecraft.toolrental.adapters.in.cli;

import com.costacodecraft.toolrental.application.domain.exception.InvalidDataEntryException;
import com.costacodecraft.toolrental.application.domain.exception.InvalidDiscountException;
import com.costacodecraft.toolrental.application.domain.exception.InvalidRentalDayException;
import com.costacodecraft.toolrental.application.domain.exception.ToolNotFoundException;
import com.costacodecraft.toolrental.application.domain.model.Order.OrderId;
import com.costacodecraft.toolrental.application.port.in.CheckoutCommand;
import com.costacodecraft.toolrental.application.port.in.CheckoutUseCase;
import com.costacodecraft.toolrental.application.port.in.GetOrderUseCase;
import com.costacodecraft.toolrental.application.port.in.GetToolUseCase;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

/**
 * Command-line interface for processing tool rentals.
 */
@Service
public class PosTerminal implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(PosTerminal.class);
  private static final String WELCOME = """
      ***************************
      *  Tool Rental Depot POS  *
      ***************************
      """;
  private static final String INVALID_CHOICE_MESSAGE = "Invalid choice. Please try again.";
  private static final String EXIT_MESSAGE = "Exiting...";

  private final CheckoutUseCase checkoutUseCase;
  private final GetOrderUseCase orderUseCase;
  private final GetToolUseCase toolUseCase;
  private final Scanner scanner;

  public PosTerminal(CheckoutUseCase checkoutUseCase, GetOrderUseCase orderUseCase,
      GetToolUseCase toolUseCase, Scanner scanner) {
    this.scanner = scanner;
    this.checkoutUseCase = checkoutUseCase;
    this.orderUseCase = orderUseCase;
    this.toolUseCase = toolUseCase;
  }


  /**
   * Runs the command-line interface.
   *
   * @param args the command-line arguments
   * @throws Exception if an error occurs
   */
  @Override
  public void run(String... args) throws Exception {
    try {
      displayMainMenu();
    } finally {
      scanner.close();
    }
  }

  /**
   * Displays the main menu and handles user input.
   */
  private void displayMainMenu() {
    System.out.println(WELCOME);
    boolean continueRunning = true;
    while (continueRunning) {
      try {
        displayToolList();
        displayMenuOptions();
        String choice = scanner.next();
        scanner.nextLine(); // Consume newline
        continueRunning = handleMenuChoice(choice);
      } catch (InvalidDataEntryException | InvalidDiscountException |
               InvalidRentalDayException | ToolNotFoundException e) {
        System.err.println(e.getMessage());
      }
    }
    System.out.println(EXIT_MESSAGE);
  }

  /**
   * Displays the menu options.
   */
  private void displayMenuOptions() {
    System.out.println("1. Rent a Tool");
    System.out.println("2. Order Lookup");
    System.out.println("3. Exit");
    System.out.print("Choose an option: ");
  }

  /**
   * Handles the user's menu choice.
   *
   * @param choice the user's menu choice
   * @return true if the menu should be displayed again, false to exit
   */
  private boolean handleMenuChoice(String choice) {
    if (choice == null) {
      return false;
    }
    return switch (choice) {
      case "1" -> {
        checkoutForm();
        yield true;
      }
      case "2" -> {
        orderLookupForm();
        yield true;
      }
      case "3" -> false;
      default -> {
        System.out.println(INVALID_CHOICE_MESSAGE);
        yield true;
      }
    };
  }

  /**
   * Displays the list of tools.
   */
  private void displayToolList() {
    System.out.printf("%-10s %10s %10s%n", "Tool Code", "Tool Type", "Price");
    System.out.println("-".repeat(35));
    toolUseCase.getAllTools()
        .forEach(tool -> {
          System.out.printf("%-10s %10s %10.2f%n", tool.id().code(), tool.type().getType(),
              tool.price().dailyCharge());
        });
  }

  /**
   * Handles the checkout form.
   */
  private void checkoutForm() {
    System.out.print("Enter the tool code: ");
    String code = scanner.nextLine();
    System.out.print("Enter rental day count (1 or greater): ");
    int dayCount = ParserHelper.parseInt(scanner.next());
    System.out.print("Enter discount percentage (range 0-100): ");
    int percentage = ParserHelper.parseInt(scanner.next());
    System.out.print("Enter checkout date (mm/dd/yy): ");
    LocalDate checkoutDate = ParserHelper.parseDate(scanner.next());
    CheckoutCommand checkoutCommand = new CheckoutCommand(code, dayCount, percentage, checkoutDate);
    List<OrderId> orderIds = checkoutUseCase.checkout(List.of(checkoutCommand));
    displayOrderDetails(orderIds);
  }

  /**
   * Handles the order lookup form.
   */
  private void orderLookupForm() {
    System.out.print("Enter Order ID: ");
    UUID id = ParserHelper.parseUUID(scanner.nextLine());
    displayOrderDetails(List.of(new OrderId(id)));
  }

  /**
   * Displays the details of the specified orders.
   *
   * @param orderIds the IDs of the orders to display
   */
  private void displayOrderDetails(List<OrderId> orderIds) {
    orderIds.stream()
        .map(orderUseCase::getOrder)
        .flatMap(Optional::stream)
        .forEach(order -> {
          System.out.println("*".repeat(35));
          System.out.println(order.id().value());
          System.out.println(order.rentalAgreement().getPrettyPrintText());
          System.out.println("*".repeat(35));
        });
  }
}