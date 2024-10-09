package com.costacodecraft.toolrental.application.port.in;

import java.time.LocalDate;

/**
 * Use case for calculating the number of chargeable days within a date range.
 */
public interface GetChargeableDaysUseCase {

  /**
   * Calculates the number of chargeable days between the specified start and end dates. Charge days
   * - Count of chargeable days, from the day after checkout (startDate) through and including the
   * due date (endDate), excluding "no charge" days as specified by the tool type.
   *
   * @param startDate the start date (checkout date)
   * @param endDate the end date (due date)
   * @return a ChargeableDaysCount object containing the counts of weekdays, weekends, and holidays
   */
  ChargeableDaysCount getChargeableDays(LocalDate startDate, LocalDate endDate);
}