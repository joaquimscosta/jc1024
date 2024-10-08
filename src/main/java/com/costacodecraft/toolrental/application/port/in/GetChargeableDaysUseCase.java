package com.costacodecraft.toolrental.application.port.in;

import java.time.LocalDate;

/**
 * Use case for calculating the number of chargeable days within a date range.
 */
public interface GetChargeableDaysUseCase {

  /**
   * Calculates the number of chargeable days between the specified start and end dates.
   *
   * @param startDate the start date
   * @param endDate the end date
   * @return a ChargeableDaysCount object containing the counts of weekdays, weekends, and holidays
   */
  ChargeableDaysCount getChargeableDays(LocalDate startDate, LocalDate endDate);
}