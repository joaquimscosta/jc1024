package com.costacodecraft.toolrental.application.domain.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;

/**
 * Represents a rental agreement for a tool, including details such as tool code, type, brand,
 * rental days, charges, and discount information.
 */
public record RentalAgreement(
    String toolCode,
    ToolType toolType,
    BrandName toolBrand,
    int rentalDays,
    LocalDate checkoutDate,
    LocalDate returnDate,
    double dailyChargeAmount,
    int chargeDays,
    BigDecimal preDiscountAmount,
    int discountPercent,
    BigDecimal discountAmount,
    BigDecimal finalChargeAmount
) {

  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Returns a formatted string representation of the rental agreement.
   *
   * @return a pretty-printed text of the rental agreement
   */
  public String getPrettyPrintText() {
    Currency currency = Currency.getInstance("USD");
    NumberFormat nf = NumberFormat.getCurrencyInstance();
    nf.setCurrency(currency);
    DateTimeFormatter df = DateTimeFormatter.ofPattern("M/d/yy");
    return "Tool Code: " + toolCode
        + "\nTool Type: " + toolType.getType()
        + "\nTool Brand: " + toolBrand.getName()
        + "\nRental Days: " + rentalDays
        + "\nCheckout Date: " + df.format(checkoutDate)
        + "\nDue Date: " + df.format(returnDate)
        + "\nDaily Rental Charge: " + nf.format(dailyChargeAmount)
        + "\nCharge Days: " + chargeDays
        + "\nPre-discount Amount: " + nf.format(preDiscountAmount)
        + "\nDiscount Percent: " + String.format("%d%%", discountPercent)
        + "\nDiscount Amount: " + nf.format(discountAmount)
        + "\nFinal Charge: " + nf.format(finalChargeAmount);
  }

  /**
   * Builder class for constructing instances of RentalAgreement.
   */
  public static final class Builder {

    private String toolCode;
    private ToolType toolType;
    private BrandName toolBrand;
    private int rentalDays;
    private LocalDate checkoutDate;
    private LocalDate returnDate;
    private double dailyChargeAmount;
    private int chargeDays;
    private BigDecimal preDiscountAmount;
    private int discountPercent;
    private BigDecimal discountAmount;
    private BigDecimal finalChargeAmount;

    private Builder() {
    }

    public Builder toolCode(String value) {
      toolCode = value;
      return this;
    }

    public Builder toolType(ToolType value) {
      toolType = value;
      return this;
    }

    public Builder toolBrand(BrandName value) {
      toolBrand = value;
      return this;
    }

    public Builder rentalDays(int value) {
      rentalDays = value;
      return this;
    }

    public Builder checkoutDate(LocalDate value) {
      checkoutDate = value;
      return this;
    }

    public Builder returnDate(LocalDate value) {
      returnDate = value;
      return this;
    }

    public Builder dailyChargeAmount(double value) {
      dailyChargeAmount = value;
      return this;
    }

    public Builder chargeDays(int value) {
      chargeDays = value;
      return this;
    }

    public Builder preDiscountAmount(BigDecimal value) {
      preDiscountAmount = value;
      return this;
    }

    public Builder discountPercent(int value) {
      discountPercent = value;
      return this;
    }

    public Builder discountAmount(BigDecimal value) {
      discountAmount = value;
      return this;
    }

    public Builder finalChargeAmount(BigDecimal value) {
      finalChargeAmount = value;
      return this;
    }

    public RentalAgreement build() {
      return new RentalAgreement(
          toolCode,
          toolType,
          toolBrand,
          rentalDays,
          checkoutDate,
          returnDate,
          dailyChargeAmount,
          chargeDays,
          preDiscountAmount,
          discountPercent,
          discountAmount,
          finalChargeAmount
      );
    }
  }
}
