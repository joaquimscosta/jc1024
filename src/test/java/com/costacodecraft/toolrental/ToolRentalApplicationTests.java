package com.costacodecraft.toolrental;

import static org.mockito.Mockito.when;

import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest()
class ToolRentalApplicationTests {

  @MockBean
  private Scanner scanner;

  @BeforeEach
  void setUp() {
    when(scanner.next()).thenReturn("3");
    when(scanner.nextLine()).thenReturn("\n");
  }

  @Test
  void contextLoads() {
    System.out.println("Context Loaded!");
  }
}