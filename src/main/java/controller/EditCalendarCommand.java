package controller;

import model.IMultiCalendarService;

/**
 * A command class for editing properties of an existing calendar.
 */
public class EditCalendarCommand implements Command {

  private IMultiCalendarService multiCalendarService;
  private String calendarName;
  private String property;
  private String newValue;

  /**
   * Constructor of the class.
   *
   * @param multiCalendarService The service managing multiple calendars
   * @param calendarName         The name of the calendar to edit
   * @param property             The property to change
   * @param newValue             The new value for the specified property
   */
  public EditCalendarCommand(IMultiCalendarService multiCalendarService, String calendarName,
      String property, String newValue) {
    this.multiCalendarService = multiCalendarService;
    this.calendarName = calendarName;
    this.property = property;
    this.newValue = newValue;
  }

  /**
   * Executes the calendar edit operation.
   *
   * @return A string indicating the result of the edit operation
   * @throws Exception if an unexpected error occurs during execution
   */
  @Override
  public String execute() throws Exception {
    if (property.equalsIgnoreCase("name")) {
      if (!multiCalendarService.editCalendar(calendarName, property, newValue)) {
        return "Calendar name already exists or edit failed.";
      }
      return "Calendar name updated to: " + newValue;
    } else if (property.equalsIgnoreCase("timezone")) {
      if (!multiCalendarService.editCalendar(calendarName, property, newValue)) {
        return "Invalid timezone: " + newValue;
      }
      return "Calendar timezone updated to: " + newValue;
    }
    return "Unknown calendar property: " + property;
  }
}