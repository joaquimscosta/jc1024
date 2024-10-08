package com.costacodecraft.toolrental.adapters.in.cli;

import com.costacodecraft.toolrental.application.domain.exception.InvalidDataEntryException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

/**
 * Helper class for parsing various data types. This class provides utility methods for parsing
 * strings into different data types such as integers, UUIDs, and dates.
 */
public class ParserHelper {

  // This class should not allow instantiation
  private ParserHelper() {
  }

  /**
   * Checks if the given string consists entirely of digits.
   *
   * @param value the string to check
   * @return true if the string is a digit, false otherwise
   */
  public static boolean isDigit(String value) {
    if (value == null || value.isEmpty()) {
      return false;
    }
    return value.matches("\\d+");
  }

  /**
   * Parses the given string into an integer.
   *
   * @param value the string to parse
   * @return the parsed integer
   * @throws InvalidDataEntryException if the string is not a valid integer
   */
  public static int parseInt(String value) {
    if (!isDigit(value)) {
      throw new InvalidDataEntryException("Invalid integer value: " + value);
    }
    return Integer.parseInt(value);
  }

  /**
   * Parses the given string into a UUID.
   *
   * @param value the string to parse
   * @return the parsed UUID
   * @throws InvalidDataEntryException if the string is not a valid UUID
   */
  public static UUID parseUUID(String value) {
    if (value == null || value.isBlank()) {
      throw new InvalidDataEntryException("Invalid UUID value: " + value);
    }
    try {
      return UUID.fromString(value);
    } catch (IllegalArgumentException e) {
      throw new InvalidDataEntryException("Invalid UUID format: " + value);
    }
  }

  /**
   * Parses the given string into a `LocalDate` using the specified date-time format.
   *
   * @param value the string to parse
   * @param format the date format to use for parsing
   * @return the parsed `LocalDate`, or null if parsing fails or the input is null/empty
   * @throws InvalidDataEntryException if the string is not a valid date
   */
  public static LocalDate parseDate(String value, String format) {
    if (value == null || value.isEmpty()) {
      throw new InvalidDataEntryException("Invalid date value: " + value);
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    try {
      return LocalDate.parse(value, formatter);
    } catch (DateTimeParseException e) {
      throw new InvalidDataEntryException(
          "Invalid date format: " + value + ". Expected format: " + format);
    }
  }

  /**
   * Parses the given string into a `LocalDate` using the default date-time format (M/d/yy).
   *
   * @param value the string to parse
   * @return the parsed `LocalDate`, or null if parsing fails or the input is null/empty
   * @throws InvalidDataEntryException if the string is not a valid date
   */
  public static LocalDate parseDate(String value) {
    if (value == null || value.isEmpty()) {
      throw new InvalidDataEntryException("Invalid date value: " + value);
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy");
    try {
      return LocalDate.parse(value, formatter);
    } catch (DateTimeParseException e) {
      throw new InvalidDataEntryException(
          "Invalid date format: " + value + ". Expected format: mm/dd/yy");
    }
  }
}