package com.costacodecraft.toolrental.application.domain.service;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.costacodecraft.toolrental.application.domain.model.Order.OrderId;
import com.costacodecraft.toolrental.application.domain.model.Tool.ToolId;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import com.costacodecraft.toolrental.application.domain.exception.ToolNotFoundException;
import com.costacodecraft.toolrental.application.domain.model.*;
import com.costacodecraft.toolrental.application.port.in.*;
import com.costacodecraft.toolrental.application.port.out.ToolRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceTest {

  @Mock
  private ToolRepository toolRepository;

  @Mock
  private CreateOrderUseCase createOrderUseCase;

  @Mock
  private GetChargeableDaysUseCase chargeableDaysUseCase;

  @Mock
  private CheckoutValidator checkoutValidator;

  private CheckoutUseCase checkoutUseCase;

  @Captor
  private ArgumentCaptor<RentalAgreement> rentalAgreementCaptor;

  @BeforeEach
  void setUp() {
    checkoutUseCase = new CheckoutService(toolRepository, createOrderUseCase, chargeableDaysUseCase, checkoutValidator);
  }

  @Test
  void testSuccessfulCheckout() {
    CheckoutCommand command = new CheckoutCommand("LADW", 5, 10, LocalDate.now());
    Tool tool = toolFactory("LADW");
    RentalAgreement rentalAgreement = mock(RentalAgreement.class);
    when(toolRepository.findById(tool.id())).thenReturn(Optional.of(tool));
    when(chargeableDaysUseCase.getChargeableDays(any(LocalDate.class), any(LocalDate.class)))
        .thenReturn(new ChargeableDaysCount(5, 0, 0));
    when(createOrderUseCase.createOrder(any(RentalAgreement.class)))
        .thenReturn(new Order(OrderId.autoGenerate(), rentalAgreement));

    List<OrderId> orderIds = checkoutUseCase.checkout(List.of(command));

    assertThat(orderIds).isNotNull();
    assertThat(orderIds).hasSize(1);
  }

  @Test
  void testToolNotFound() {
    CheckoutCommand command = new CheckoutCommand("INVALID", 5, 10, LocalDate.now());
    when(toolRepository.findById(new ToolId("INVALID"))).thenReturn(Optional.empty());

    assertThatExceptionOfType(ToolNotFoundException.class)
        .isThrownBy(() -> checkoutUseCase.checkout(List.of(command)))
        .withMessage("Tool with code \"INVALID\" not found");
  }

  @Test
  void testRentalAgreementCreation() {
    var toolCode = "ABC";
    var checkoutDate = LocalDate.now();
    var discount = 10;
    var rentalDays = 5;
    CheckoutCommand command = new CheckoutCommand(toolCode, rentalDays, discount, checkoutDate);
    Tool tool = toolFactory(toolCode);
    String toolType = tool.type().getType();
    String toolBrand = tool.brand().getName();

    when(toolRepository.findById(tool.id())).thenReturn(Optional.of(tool));
    when(chargeableDaysUseCase.getChargeableDays(any(LocalDate.class), any(LocalDate.class)))
        .thenReturn(new ChargeableDaysCount(5, 0, 0));
    when(createOrderUseCase.createOrder(any(RentalAgreement.class)))
        .thenReturn(new Order(OrderId.autoGenerate(), any(RentalAgreement.class)));

    checkoutUseCase.checkout(List.of(command));

    verify(createOrderUseCase).createOrder(rentalAgreementCaptor.capture());
    RentalAgreement rentalAgreement = rentalAgreementCaptor.getValue();

    assertThat(rentalAgreement).isNotNull();
    assertThat(rentalAgreement.toolCode()).isEqualTo(toolCode);
    assertThat(rentalAgreement.toolType().getType()).isEqualTo(toolType);
    assertThat(rentalAgreement.toolBrand().getName()).isEqualTo(toolBrand);
    assertThat(rentalAgreement.rentalDays()).isEqualTo(rentalDays);
    assertThat(rentalAgreement.checkoutDate()).isEqualTo(checkoutDate);
    assertThat(rentalAgreement.returnDate()).isEqualTo(checkoutDate.plusDays(rentalDays));
    assertThat(rentalAgreement.dailyChargeAmount()).isEqualTo(tool.price().dailyCharge());
    assertThat(rentalAgreement.chargeDays()).isEqualTo(5);
    assertThat(rentalAgreement.preDiscountAmount()).isEqualByComparingTo(new BigDecimal("9.95"));
    assertThat(rentalAgreement.discountPercent()).isEqualTo(discount);
    assertThat(rentalAgreement.discountAmount()).isEqualByComparingTo(new BigDecimal("1.00"));
    assertThat(rentalAgreement.finalChargeAmount()).isEqualByComparingTo(new BigDecimal("8.95"));
  }

  private static Tool toolFactory(String code){
    return new Tool(
        new ToolId(code),
        ToolType.CHAINSAW,
        BrandName.STIHL,
        Price.newBuilder()
            .dailyCharge(1.99)
            .build()
    );
  }

}
