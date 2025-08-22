package controller;

import java.time.LocalDateTime;
import model.ICalendarService;

/**
 * Command to print events within a specified date-time range.
 */
public class PrintEventsRangeCommand implements Command {

  private ICalendarService calendarService;
  private LocalDateTime start;
  private LocalDateTime end;

  /**
   * Constructs a command to print events between two date-times.
   *
   * @param calendarService The service managing calendar events
   * @param startDTStr      The start date-time string to parse
   * @param endDTStr        The end date-time string to parse
   */
  public PrintEventsRangeCommand(ICalendarService calendarService,
      String startDTStr, String endDTStr) {
    try {
      this.calendarService = calendarService;
      this.start = CommandParserStatic.parseDateTimeStatic(startDTStr);
      this.end = CommandParserStatic.parseDateTimeStatic(endDTStr);
    } catch (model.InvalidDateException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Executes the command by retrieving events within the range and formatting them.
   *
   * @return A formatted string of events within the specified range
   * @throws Exception if an error occurs during execution
   */
  @Override
  public String execute() throws Exception {
    return calendarService.printEventsRange(start, end);
  }
}