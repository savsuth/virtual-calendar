package controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import model.InvalidDateException;

/**
 * Provides static helper methods for parsing date and time strings.
 */
public class CommandParserStatic {

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

  /**
   * Parses a date-time string which may contain only a date or both date and time. For an all-day
   * event (only date provided), midnight is assumed as the time.
   *
   * @param dateTimeStr the date-time string to parse
   * @return the parsed LocalDateTime object
   * @throws InvalidDateException if the string does not match the expected format
   */
  public static LocalDateTime parseDateTimeStatic(String dateTimeStr) throws InvalidDateException {
    try {
      String[] parts = dateTimeStr.split("T");
      if (parts.length == 1) {
        LocalDate date = LocalDate.parse(parts[0], DATE_FORMAT);
        return LocalDateTime.of(date, LocalTime.MIDNIGHT);
      } else if (parts.length == 2) {
        LocalDate date = LocalDate.parse(parts[0], DATE_FORMAT);
        LocalTime time = LocalTime.parse(parts[1], TIME_FORMAT);
        return LocalDateTime.of(date, time);
      } else {
        throw new InvalidDateException("Invalid date format. Expected 'YYYY-MM-DD'");
      }
    } catch (DateTimeParseException e) {
      throw new InvalidDateException("Invalid date format. Expected 'YYYY-MM-DD'");
    }
  }
}
