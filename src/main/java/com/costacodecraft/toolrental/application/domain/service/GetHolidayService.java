package com.costacodecraft.toolrental.application.domain.service;

import com.costacodecraft.toolrental.application.port.in.GeHolidayUseCase;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for retrieving holidays within a date range.
 */
@Service
public class GetHolidayService implements GeHolidayUseCase {

  private static final Logger LOGGER = LoggerFactory.getLogger(GetHolidayService.class);

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException if startDate or endDate is null, or if startDate is not before
   * endDate
   */
  @Override
  public Set<LocalDate> getHolidays(LocalDate startDate, LocalDate endDate) {
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException("Start date and end date cannot be null");
    }
    if (!startDate.isBefore(endDate)) {
      throw new IllegalArgumentException("Start date must be before end date");
    }
    LOGGER.debug("Calculating holidays from {} to {}", startDate, endDate);
    Set<LocalDate> holidays = new HashSet<>();
    for (int year = startDate.getYear(); year <= endDate.getYear(); year++) {
      holidays.add(getObservedIndependenceDay(year));
      holidays.add(getLaborDay(year));
    }
    LOGGER.debug("Holidays calculated: {}", holidays);
    return holidays;
  }

  /**
   * Gets the observed Independence Day for the specified year.
   *
   * @param year the year
   * @return the observed Independence Day
   */
  private LocalDate getObservedIndependenceDay(int year) {
    LocalDate independenceDay = LocalDate.of(year, Month.JULY, 4);
    DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();
    if (dayOfWeek == DayOfWeek.SATURDAY) {
      // observed on Friday
      return independenceDay.minusDays(1);
    } else if (dayOfWeek == DayOfWeek.SUNDAY) {
      // observed on Monday
      return independenceDay.plusDays(1);
    } else {
      // observed on the actual date
      return independenceDay;
    }
  }

  /**
   * Gets Labor Day for the specified year.
   *
   * @param year the year
   * @return Labor Day
   */
  private LocalDate getLaborDay(int year) {
    return LocalDate.of(year, Month.SEPTEMBER, 1)
        .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
  }
}