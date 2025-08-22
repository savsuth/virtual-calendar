package controller;

import java.time.LocalDate;
import model.ICalendarService;

/**
 * Command to print events for a specific date.
 */
public class PrintEventsCommand implements Command {

  private ICalendarService calendarService;
  private LocalDate date;

  /**
   * Constructs a command to print events on a given date.
   *
   * @param service The calendar service to query
   * @param date    The date to retrieve events for
   */
  public PrintEventsCommand(ICalendarService service, LocalDate date) {
    this.calendarService = service;
    this.date = date;
  }

  /**
   * Executes the command by retrieving and formatting events for the specified date.
   *
   * @return A formatted string of events on the given date
   * @throws Exception if an error occurs during execution
   */
  @Override
  public String execute() throws Exception {
    return calendarService.printEventsOn(date);
  }
}