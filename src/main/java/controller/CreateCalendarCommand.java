package controller;

import model.IMultiCalendarService;

/**
 * A command class for creating a new calendar with a specified name and timezone. Implements the
 * Command pattern to encapsulate calendar creation logic.
 */
public class CreateCalendarCommand implements Command {

  private IMultiCalendarService multiCalendarService;
  private String calendarName;
  private String timezone;

  /**
   * Constructor for the class.
   *
   * @param multiCalendarService The service managing multiple calendars
   * @param calendarName         The name for the new calendar
   * @param timezone             The timezone to associate with the calendar
   */
  public CreateCalendarCommand(IMultiCalendarService multiCalendarService,
      String calendarName, String timezone) {
    this.multiCalendarService = multiCalendarService;
    this.calendarName = calendarName;
    this.timezone = timezone;
  }

  /**
   * Executes the calendar creation operation.
   *
   * @return A string indicating the result of the calendar creation
   * @throws Exception if an unexpected error occurs during execution
   */
  @Override
  public String execute() throws Exception {
    try {
      boolean success = multiCalendarService.createCalendar(calendarName, timezone);
      if (!success) {
        return "Calendar creation failed: Duplicate calendar name.";
      }
      return "Calendar created: " + calendarName;
    } catch (Exception e) {
      return "Calendar creation failed: " + e.getMessage();
    }
  }
}