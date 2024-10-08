package com.costacodecraft.toolrental;

import java.util.Scanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main application class for the Tool Rental application.
 */
@SpringBootApplication
public class ToolRentalApplication {

  public static void main(String[] args) {
    SpringApplication.run(ToolRentalApplication.class, args);
  }

  @Bean
  Scanner scanner() {
    return new Scanner(System.in);
  }

}
