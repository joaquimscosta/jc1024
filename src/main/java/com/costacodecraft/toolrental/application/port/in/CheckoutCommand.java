package com.costacodecraft.toolrental.application.port.in;

import java.time.LocalDate;

/**
 * Command representing the details required for checking out a tool.
 *
 * @param toolCode the code of the tool to be rented
 * @param rentalDays the number of days the tool will be rented
 * @param discountPercentage the discount percentage to be applied
 * @param checkoutDate the date when the tool is checked out
 */
public record CheckoutCommand(
    String toolCode,
    int rentalDays,
    int discountPercentage,
    LocalDate checkoutDate
) {

}
