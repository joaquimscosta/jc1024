package com.costacodecraft.toolrental.application.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.when;

import com.costacodecraft.toolrental.application.port.in.ChargeableDaysCount;
import com.costacodecraft.toolrental.application.port.in.GeHolidayUseCase;
import com.costacodecraft.toolrental.application.port.in.GetChargeableDaysUseCase;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GetChargeableDaysServiceTest {

  @Mock
  private GeHolidayUseCase holidayUseCase;

  private GetChargeableDaysUseCase getChargeableDaysUseCase;

  @BeforeEach
  void setUp() {
    getChargeableDaysUseCase = new GetChargeableDaysService(holidayUseCase);
  }

  @Test
  void testValidDateRangeWithNoHolidays() {
    LocalDate startDate = LocalDate.of(2024, Month.JANUARY, 1);
    LocalDate endDate = LocalDate.of(2024, Month.JANUARY, 7);
    when(holidayUseCase.getHolidays(startDate, endDate)).thenReturn(Set.of());

    ChargeableDaysCount chargeableDaysCount = getChargeableDaysUseCase.getChargeableDays(startDate, endDate);

    assertThat(chargeableDaysCount).isNotNull();
    assertThat(chargeableDaysCount.weekdayCount()).isEqualTo(4);
    assertThat(chargeableDaysCount.weekendCount()).isEqualTo(2);
    assertThat(chargeableDaysCount.holidayCount()).isEqualTo(0);
  }

  @Test
  void testValidDateRangeWithHolidays() {
    LocalDate startDate = LocalDate.of(2024, Month.DECEMBER, 24);
    LocalDate endDate = LocalDate.of(2024, Month.DECEMBER, 26);
    when(holidayUseCase.getHolidays(startDate, endDate)).thenReturn(Set.of(LocalDate.of(2024, Month.DECEMBER, 25)));

    ChargeableDaysCount chargeableDaysCount = getChargeableDaysUseCase.getChargeableDays(startDate, endDate);

    assertThat(chargeableDaysCount).isNotNull();
    assertThat(chargeableDaysCount.weekdayCount()).isEqualTo(1);
    assertThat(chargeableDaysCount.weekendCount()).isEqualTo(0);
    assertThat(chargeableDaysCount.holidayCount()).isEqualTo(1);
  }

  @Test
  void testStartDateEqualsEndDate() {
    LocalDate startDate = LocalDate.of(2024, Month.JANUARY, 1);
    LocalDate endDate = LocalDate.of(2024, Month.JANUARY, 1);

    assertThatIllegalArgumentException()
        .isThrownBy(() -> getChargeableDaysUseCase.getChargeableDays(startDate, endDate));
  }

  @Test
  void testNullStartDate() {
    LocalDate endDate = LocalDate.of(2024, Month.JANUARY, 1);

    assertThatIllegalArgumentException()
        .isThrownBy(() -> getChargeableDaysUseCase.getChargeableDays(null, endDate));
  }

  @Test
  void testNullEndDate() {
    LocalDate startDate = LocalDate.of(2024, Month.JANUARY, 1);

    assertThatIllegalArgumentException()
        .isThrownBy(() -> getChargeableDaysUseCase.getChargeableDays(startDate, null));
  }
}