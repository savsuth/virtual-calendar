package controller;

import model.IMultiCalendarService;

/**
 * Command to switch the active calendar in a multi-calendar system.
 */
public class UseCalendarCommand implements Command {

  private IMultiCalendarService multiCalendarService;
  private String calendarName;

  /**
   * Constructs a command to switch to a specified calendar.
   *
   * @param multiCalendarService The service managing multiple calendars
   * @param calendarName         The name of the calendar to use
   */
  public UseCalendarCommand(IMultiCalendarService multiCalendarService, String calendarName) {
    this.multiCalendarService = multiCalendarService;
    this.calendarName = calendarName;
  }

  /**
   * Executes the command to set the active calendar.
   *
   * @return A message indicating the switch result
   * @throws Exception if an error occurs during execution
   */
  @Override
  public String execute() throws Exception {
    if (!multiCalendarService.useCalendar(calendarName)) {
      return "Calendar not found: " + calendarName;
    }
    multiCalendarService.useCalendar(calendarName);
    return "Using calendar: " + calendarName;
  }
}
