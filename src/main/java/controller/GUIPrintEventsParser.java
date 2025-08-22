package controller;

import java.time.LocalDate;
import model.IMultiCalendarService;

/**
 * Parses print event commands from GUI input.
 */
public class GUIPrintEventsParser {

  private IMultiCalendarService calendarService;


  /**
   * Constructs a GUIPrintEventsParser with the given calendar service.
   *
   * @param service the calendar service
   */
  public GUIPrintEventsParser(IMultiCalendarService service) {
    this.calendarService = service;
  }

  /**
   * Parses a command to print events on the specified date.
   *
   * @param date the date to print events for
   * @return a {@link Command} that prints the events
   */
  public Command parse(LocalDate date) {
    try {
      if (date == null) {
        return () -> "Error: Date is required.";
      }
      return new PrintEventsCommand(calendarService, date);
    } catch (Exception e) {
      return () -> "Error processing print command: " + e.getMessage();
    }
  }
}
