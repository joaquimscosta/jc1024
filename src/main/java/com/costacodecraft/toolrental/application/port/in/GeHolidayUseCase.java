package com.costacodecraft.toolrental.application.port.in;

import java.time.LocalDate;
import java.util.Set;

/**
 * Use case for retrieving holidays within a date range.
 */
public interface GeHolidayUseCase {

  /**
   * Retrieves holidays between the specified start and end dates.
   *
   * @param startDate the start date
   * @param endDate the end date
   * @return a set of holidays within the date range
   */
  Set<LocalDate> getHolidays(LocalDate startDate, LocalDate endDate);
}
