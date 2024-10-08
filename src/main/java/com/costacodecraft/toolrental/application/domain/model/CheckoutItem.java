package com.costacodecraft.toolrental.application.domain.model;

import static java.util.Objects.requireNonNull;

import com.costacodecraft.toolrental.application.port.in.ChargeableDaysCount;
import com.costacodecraft.toolrental.application.port.in.CheckoutCommand;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an item being checked out, including details such as the tool, rental days, discount
 * percentage, checkout date, and chargeable days count.
 */
public record CheckoutItem(
    Tool tool,
    int rentalDays,
    int discountPercentage,
    LocalDate checkoutDate,
    ChargeableDaysCount chargeableDaysCount
) {

  private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutItem.class);

  public CheckoutItem {
    requireNonNull(tool, "tool cannot be null");
    requireNonNull(checkoutDate, "checkoutDate cannot be null");
    requireNonNull(chargeableDaysCount, "chargeableDaysCount cannot be null");
  }

  public CheckoutItem(Tool tool, CheckoutCommand checkoutCommand,
      ChargeableDaysCount chargeableDaysCount) {
    this(tool, checkoutCommand.rentalDays(), checkoutCommand.discountPercentage(),
        checkoutCommand.checkoutDate(), chargeableDaysCount);
  }

  /**
   * Returns the count of chargeable days, from the day after checkout through and including the due
   * date, excluding "no charge" days as specified by the tool type.
   *
   * @return the count of chargeable days
   */
  public int getChargeableDaysCount() {
    LOGGER.debug("Calculating chargeable days");
    int weekdayCount = chargeableDaysCount.weekdayCount();
    int weekendCount = chargeableDaysCount.weekendCount();
    int holidayCount = chargeableDaysCount.holidayCount();
    Price price = tool.price();
    int chargeableDays = 0;
    if (price.weekdayCharge()) {
      chargeableDays += weekdayCount;
    }
    if (price.weekendCharge()) {
      chargeableDays += weekendCount;
    }
    if (price.holidayCharge()) {
      chargeableDays += holidayCount;
    }
    LOGGER.debug("Chargeable days calculated: {}", chargeableDays);
    return chargeableDays;
  }

  /**
   * Returns the return date calculated from the checkout date and rental days.
   *
   * @return the return date
   */
  public LocalDate getReturnDate() {
    LOGGER.debug("Calculating return date");
    LocalDate returnDate = checkoutDate.plusDays(rentalDays);
    LOGGER.debug("Return date calculated: {}", returnDate);
    return returnDate;
  }

  /**
   * Calculates the pre-discount charge as chargeable days multiplied by the daily charge. The
   * resulting total is rounded half up to cents.
   *
   * @return the pre-discount amount
   */
  public BigDecimal getPreDiscountAmount() {
    LOGGER.debug("Calculating pre-discount amount");
    BigDecimal dailyCharge = BigDecimal.valueOf(tool.price().dailyCharge());
    BigDecimal chargeableDays = BigDecimal.valueOf(getChargeableDaysCount());
    BigDecimal preDiscountAmount = dailyCharge.multiply(chargeableDays)
        .setScale(2, RoundingMode.HALF_UP);
    LOGGER.debug("Pre-discount amount calculated: {}", preDiscountAmount);
    return preDiscountAmount;
  }

  /**
   * Calculates the discount amount from the discount percentage and pre-discount charge. The
   * resulting amount is rounded half up to cents.
   *
   * @return the discount amount
   */
  public BigDecimal getDiscountAmount() {
    LOGGER.debug("Calculating discount amount");
    BigDecimal preDiscountAmount = getPreDiscountAmount();
    double percent = discountPercentage/100.0;
    BigDecimal discountAmount = preDiscountAmount.multiply(BigDecimal.valueOf(percent))
        .setScale(2, RoundingMode.HALF_UP);
    LOGGER.debug("Discount amount calculated: {}", discountAmount);
    return discountAmount;
  }

  /**
   * Calculates the final charge as pre-discount charge minus discount amount.
   *
   * @return the final charge amount
   */
  public BigDecimal getFinalChargeAmount() {
    LOGGER.debug("Calculating final charge amount");
    BigDecimal finalChargeAmount = getPreDiscountAmount().subtract(getDiscountAmount())
        .setScale(2, RoundingMode.HALF_UP);
    LOGGER.debug("Final charge amount calculated: {}", finalChargeAmount);
    return finalChargeAmount;
  }
}
