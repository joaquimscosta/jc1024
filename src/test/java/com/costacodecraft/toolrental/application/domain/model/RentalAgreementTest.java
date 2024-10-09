package com.costacodecraft.toolrental.application.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.Test;

public class RentalAgreementTest {

  @Test
  void testPrettyPrintText() {
    String toolCode = "LADW";
    ToolType toolType = ToolType.LADDER;
    BrandName toolBrand = BrandName.WERNER;
    int rentalDays = 5;
    LocalDate checkoutDate = LocalDate.of(2024, Month.OCTOBER, 1);
    LocalDate returnDate = LocalDate.of(2024, Month.OCTOBER, 9);
    double dailyChargeAmount = 1.99;
    int chargeDays = 5;
    int discountPercent = 10;
    BigDecimal preDiscountAmount = new BigDecimal("9.95");
    BigDecimal discountAmount = new BigDecimal("0.99");
    BigDecimal finalChargeAmount = new BigDecimal("8.96");

    RentalAgreement rentalAgreement = RentalAgreement.newBuilder()
        .toolCode(toolCode)
        .toolType(toolType)
        .toolBrand(toolBrand)
        .rentalDays(rentalDays)
        .checkoutDate(checkoutDate)
        .returnDate(returnDate)
        .dailyChargeAmount(dailyChargeAmount)
        .chargeDays(chargeDays)
        .preDiscountAmount(preDiscountAmount)
        .discountPercent(discountPercent)
        .discountAmount(discountAmount)
        .finalChargeAmount(finalChargeAmount)
        .build();

    String expectedPrettyPrintText = "Tool Code: LADW\n" +
        "Tool Type: Ladder\n" +
        "Tool Brand: Werner\n" +
        "Rental Days: 5\n" +
        "Checkout Date: 10/1/24\n" +
        "Due Date: 10/9/24\n" +
        "Daily Rental Charge: $1.99\n" +
        "Charge Days: 5\n" +
        "Pre-discount Amount: $9.95\n" +
        "Discount Percent: 10\n" +
        "Discount Amount: $0.99\n" +
        "Final Charge: $8.96";

    String prettyPrintText = rentalAgreement.getPrettyPrintText();

    assertThat(prettyPrintText).isEqualTo(expectedPrettyPrintText);
  }
}