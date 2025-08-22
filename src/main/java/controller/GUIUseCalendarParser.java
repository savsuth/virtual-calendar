package controller;

import model.IMultiCalendarService;

/**
 * Parses use calendar commands from GUI input.
 */
public class GUIUseCalendarParser {

  private IMultiCalendarService service;

  /**
   * Constructs a GUIUseCalendarParser with the given calendar service.
   *
   * @param service the calendar service
   */
  public GUIUseCalendarParser(IMultiCalendarService service) {
    this.service = service;
  }

  /**
   * Parses a command to switch to the specified calendar.
   *
   * @param calendarName the name of the calendar to use
   * @return a {@link Command} to use the specified calendar
   */
  public Command parse(String calendarName) {
    try {
      return new UseCalendarCommand(service, calendarName);
    } catch (Exception e) {
      return () -> "Error processing use calendar command: " + e.getMessage();
    }
  }
}
