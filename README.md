# Tool Rental Application

The Tool Rental Application is a command-line interface (CLI) tool rental system built with Spring Boot. It allows users to rent tools, calculate rental charges, and manage tool inventory.

## Features

- Rent tools with specified rental days and discount percentages.
- Calculate rental charges based on tool type and rental days.
- Display rental agreements with detailed charge breakdowns.
- Manage tool inventory and display available tools.

## Getting Started

### Prerequisites

- Java 21

### Installation

1. Clone the repository:

   ```sh
   git clone https://github.com/joaquimscosta/jc1024.git
   cd jc1024
   ```

2. Ensure you have the required Java version and Gradle installed.

## Building the Project

To build the project, run the following command:

```sh
./gradlew build
```

## Running the Application

To run the application, use the following command:

```sh
java -jar ./build/libs/toolrental-0.0.1-SNAPSHOT.jar
```

## Testing

To run the tests, use the following command:

```sh
./gradlew test
```

### Integration Tests

The project includes integration tests for the `CheckoutUseCase` to ensure the correctness of the rental process. These tests cover various scenarios such as invalid discounts and rental charge calculations.

Example test case: `CheckoutServiceIntegrationTest`

- Invalid Discount: Tests that an `InvalidDiscountException` is thrown when the discount percentage is invalid.
- Holiday and Weekend Charges: Tests the implementation of holiday and weekend charges under various scenarios.

### Unit Tests

The project includes tests for core functionality and critical paths to ensure system accuracy.

## Usage

Once the application is running, you can interact with it via the command-line interface. Follow the prompts to rent tools, view rental agreements, and manage the tool inventory.

### Example Commands

- **Checkout a Tool**: Follow the prompts to enter tool code, rental days, discount percentage, and checkout date.
- **View Tools**: Display a list of available tools with their details.
- **Lookup Order**: Search for an order by its ID.

## Limitations

- **In-Memory Data Persistence**: Data is not saved between application restarts.
- **Command-Line Interface**: Limited user-friendliness compared to GUI or web interfaces.
- **Basic Error Handling**: May not cover all edge cases. For example, adding a limit on the maximum rental days to prevent unrealistic rental periods.
- **Order-Customer Association**: Orders are not associated with a customer, which limits tracking and management of customer-specific rentals.

## Future Improvements

- **Money Class**: Create a `Money` class and use it in the `CheckoutItem` when dealing with money calculations to improve code clarity and maintainability.
