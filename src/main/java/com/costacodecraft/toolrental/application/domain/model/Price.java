package com.costacodecraft.toolrental.application.domain.model;

/**
 * Represents the price details of a tool, including daily charge and charge applicability for
 * weekdays, weekends, and holidays.
 *
 * @param dailyCharge the daily charge for renting the tool
 * @param weekdayCharge whether the tool is chargeable on weekdays
 * @param weekendCharge whether the tool is chargeable on weekends
 * @param holidayCharge whether the tool is chargeable on holidays
 */
public record Price(
    double dailyCharge,
    boolean weekdayCharge,
    boolean weekendCharge,
    boolean holidayCharge
) {

  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Builder class for constructing Price instances.
   */
  public static final class Builder {

    private double dailyCharge;
    private boolean weekdayCharge = true;
    private boolean weekendCharge = true;
    private boolean holidayCharge = true;

    private Builder() {
    }

    public Builder dailyCharge(double value) {
      dailyCharge = value;
      return this;
    }

    public Builder weekdayCharge(boolean value) {
      weekdayCharge = value;
      return this;
    }

    public Builder weekendCharge(boolean value) {
      weekendCharge = value;
      return this;
    }

    public Builder holidayCharge(boolean value) {
      holidayCharge = value;
      return this;
    }

    public Price build() {
      return new Price(dailyCharge, weekdayCharge, weekendCharge, holidayCharge);
    }
  }
}
