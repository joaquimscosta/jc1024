package com.costacodecraft.toolrental.application.domain.service;

import com.costacodecraft.toolrental.application.port.in.ChargeableDaysCount;
import com.costacodecraft.toolrental.application.port.in.GeHolidayUseCase;
import com.costacodecraft.toolrental.application.port.in.GetChargeableDaysUseCase;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for calculating the number of chargeable days within a date range.
 */
@Service
public class GetChargeableDaysService implements GetChargeableDaysUseCase {

  private static final Logger LOGGER = LoggerFactory.getLogger(GetChargeableDaysService.class);

  private final GeHolidayUseCase holidayUseCase;

  public GetChargeableDaysService(GeHolidayUseCase holidayUseCase) {
    this.holidayUseCase = holidayUseCase;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException if startDate or endDate is null, or if startDate is not before
   * endDate
   */
  @Override
  public ChargeableDaysCount getChargeableDays(LocalDate startDate, LocalDate endDate) {
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException("Start date and end date cannot be null");
    }
    if (!startDate.isBefore(endDate)) {
      throw new IllegalArgumentException("Start date must be before end date");
    }
    LOGGER.debug("Calculating chargeable days from {} to {}", startDate, endDate);
    int weekdayCount = 0;
    int weekendCount = 0;
    int holidayCount = 0;
    Set<LocalDate> holidays = holidayUseCase.getHolidays(startDate, endDate);
    LocalDate currentDate = startDate.plusDays(1);
    while (!currentDate.isAfter(endDate)) {
      if (holidays.contains(currentDate)) {
        holidayCount++;
      } else {
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
        switch (dayOfWeek) {
          case SATURDAY:
          case SUNDAY:
            weekendCount++;
            break;
          default:
            weekdayCount++;
            break;
        }
      }
      currentDate = currentDate.plusDays(1);
    }
    LOGGER.debug("Chargeable days calculated: weekdays={}, weekends={}, holidays={}",
        weekdayCount, weekendCount, holidayCount);
    return new ChargeableDaysCount(weekdayCount, weekendCount, holidayCount);
  }
}
