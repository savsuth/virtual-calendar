package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Defines methods for printing events to text based on date filters.
 */
public interface IEventPrinter {

  /**
   * Prints events scheduled on the given date.
   *
   * @param date the date to print events for
   * @return string representation of events
   * @throws Exception if an error occurs
   */
  String printEventsOn(LocalDate date) throws Exception;

  /**
   * Prints events within a specific time range.
   *
   * @param start the start datetime
   * @param end   the end datetime
   * @return formatted string of events
   * @throws Exception if an error occurs
   */
  String printEventsRange(LocalDateTime start, LocalDateTime end) throws Exception;
}

