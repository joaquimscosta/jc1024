package com.costacodecraft.toolrental.application.port.in;

/**
 * Record representing the count of chargeable days for a rental.
 *
 * @param weekdayCount the number of chargeable weekdays
 * @param weekendCount the number of chargeable weekend days
 * @param holidayCount the number of chargeable holidays
 */
public record ChargeableDaysCount(
    int weekdayCount,
    int weekendCount,
    int holidayCount
) {

}