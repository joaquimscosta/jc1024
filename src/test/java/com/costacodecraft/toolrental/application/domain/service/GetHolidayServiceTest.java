package com.costacodecraft.toolrental.application.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GetHolidayServiceTest {

  private GetHolidayService getHolidayService;

  @BeforeEach
  void setUp() {
    getHolidayService = new GetHolidayService();
  }

  @Test
  void testGetHolidaysWithinRange() {
    LocalDate startDate = LocalDate.of(2024, 1, 1);
    LocalDate endDate = LocalDate.of(2024, 12, 31);
    LocalDate independenceDate = LocalDate.of(2024, 7, 4);
    LocalDate laborDay = LocalDate.of(2024, Month.SEPTEMBER, 1)
        .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));

    Set<LocalDate> holidays = getHolidayService.getHolidays(startDate, endDate);

    assertNotNull(holidays);
    assertThat(holidays).hasSize(2);
    Assertions.assertThat(holidays).containsExactlyInAnyOrder(independenceDate, laborDay);
  }

  @Test
  void testGetHolidaysWithNullStartDate() {
    LocalDate endDate = LocalDate.of(2024, 12, 31);

    assertThatIllegalArgumentException()
        .isThrownBy(() -> getHolidayService.getHolidays(null, endDate));
  }

  @Test
  void testGetHolidaysWithNullEndDate() {
    LocalDate startDate = LocalDate.of(2024, 1, 1);

    assertThatIllegalArgumentException()
        .isThrownBy(() -> getHolidayService.getHolidays(startDate, null));
  }

  @Test
  void testGetHolidaysWithStartDateAfterEndDate() {
    LocalDate startDate = LocalDate.of(2024, 12, 31);
    LocalDate endDate = LocalDate.of(2024, 1, 1);

    assertThatIllegalArgumentException()
        .isThrownBy( () -> getHolidayService.getHolidays(startDate, endDate));
  }

  @Test
  void testObservedIndependenceDayOnMonday() {
    LocalDate startDate = LocalDate.of(2021, 1, 1);
    LocalDate endDate = LocalDate.of(2021, 12, 31);
    LocalDate independenceDate = LocalDate.of(2021,7,5);

    Set<LocalDate> holidays = getHolidayService.getHolidays(startDate, endDate);

    Assertions.assertThat(holidays).isNotNull();
    Assertions.assertThat(holidays).contains(independenceDate);
  }

  @Test
  void testLaborDay() {
    LocalDate startDate = LocalDate.of(2024, 1, 1);
    LocalDate endDate = LocalDate.of(2024, 12, 31);
    LocalDate laborDay = LocalDate.of(2024, Month.SEPTEMBER, 1)
        .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));

    Set<LocalDate> holidays = getHolidayService.getHolidays(startDate, endDate);

    Assertions.assertThat(holidays).isNotNull();
    Assertions.assertThat(holidays).contains(laborDay);
  }
}