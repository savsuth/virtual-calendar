package controller;

import model.IMultiCalendarService;

/**
 * Parses input for editing calendar properties.
 */
public class GUIEditCalendarParser {

  private IMultiCalendarService service;

  /**
   * Constructs a parser for editing a calendar.
   *
   * @param service the calendar service
   */
  public GUIEditCalendarParser(IMultiCalendarService service) {
    this.service = service;
  }


  /**
   * Parses input and returns a command to edit a calendar property.
   *
   * @param calendarName name of the calendar to edit
   * @param property     property to change
   * @param newValue     new value to assign
   * @return the command to execute
   */
  public Command parse(String calendarName, String property, String newValue) {

    try {
      if (calendarName == null || calendarName.trim().isEmpty()) {
        return () -> "Error: Calendar name cannot be empty.";
      }
      if (property == null || property.trim().isEmpty()) {
        return () -> "Error: Calendar property cannot be empty.";
      }
      if (newValue == null || newValue.trim().isEmpty()) {
        return () -> "Error: New value cannot be empty.";
      }

      return new EditCalendarCommand(service, calendarName, property, newValue);
    } catch (Exception e) {
      return () -> "Error processing edit calendar command: " + e.getMessage();
    }
  }
}
