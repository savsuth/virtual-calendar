package controller;

import model.IMultiCalendarService;

/**
 * Parses input for creating a new calendar from the GUI.
 */
public class GUICreateCalendarParser {

  private IMultiCalendarService service;

  /**
   * Constructs a GUICreateCalendarParser with the given service.
   *
   * @param service the calendar service layer
   */
  public GUICreateCalendarParser(IMultiCalendarService service) {
    this.service = service;
  }

  /**
   * Parses the input and returns a command to create a new calendar.
   *
   * @param calendarName the name of the calendar to be created
   * @param timezone     the timezone of the calendar
   * @return a command to execute calendar creation
   */
  public Command parse(String calendarName, String timezone) {
    try {
      return new CreateCalendarCommand(service, calendarName, timezone);
    } catch (Exception e) {
      return () -> "Error processing create calendar command: " + e.getMessage();
    }
  }
}
