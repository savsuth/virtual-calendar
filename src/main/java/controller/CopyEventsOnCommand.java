package controller;

import java.time.LocalDate;
import model.IMultiCalendarService;

/**
 * A command class for copying all events on a specific date to another date in a target calendar.
 * It implements the Command pattern for single-day event copying operations.
 */
public class CopyEventsOnCommand implements Command {

  private IMultiCalendarService multiCalendarService;
  private LocalDate sourceDate;
  private String targetCalendarName;
  private LocalDate targetDate;

  /**
   * Constructor of the class.
   *
   * @param multiCalendarService The service handling calendar operations
   * @param sourceDate           The date containing the events to copy
   * @param targetCalendarName   The name of the calendar to copy events to
   * @param targetDate           The destination date for the copied events
   */
  public CopyEventsOnCommand(IMultiCalendarService multiCalendarService, LocalDate sourceDate,
      String targetCalendarName, LocalDate targetDate) {
    this.multiCalendarService = multiCalendarService;
    this.sourceDate = sourceDate;
    this.targetCalendarName = targetCalendarName;
    this.targetDate = targetDate;
  }

  /**
   * Executes the event copy operation for the specified date.
   *
   * @return A string result from the copy operation
   * @throws Exception if the copy operation fails
   */
  @Override
  public String execute() throws Exception {
    return multiCalendarService.copyEventsOn(sourceDate, targetCalendarName, targetDate);
  }
}