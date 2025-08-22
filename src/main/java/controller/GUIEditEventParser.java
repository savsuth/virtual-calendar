package controller;

import controller.EditEventCommand.EditMode;
import java.time.LocalDateTime;
import model.IMultiCalendarService;
import model.InvalidDateException;

/**
 * Parses input for editing an event.
 */
public class GUIEditEventParser {

  private IMultiCalendarService service;

  /**
   * Constructs a parser for editing events.
   *
   * @param service the calendar service
   */
  public GUIEditEventParser(IMultiCalendarService service) {
    this.service = service;
  }

  /**
   * Parses input and returns a command to edit an event.
   *
   * @param subject   subject of the event
   * @param strFrom   start time (required for SINGLE or FROM mode)
   * @param property  property to be edited
   * @param newValue  new value for the property
   * @param strmode   editing mode (SINGLE, FROM, ALL)
   * @return the command to execute
   */
  public Command parse(String subject, String strFrom, String property, String newValue,
      String strmode) {
    if (subject == null || subject.trim().isEmpty()) {
      return () -> "Error: Subject cannot be empty.";
    }
    if (property == null || property.trim().isEmpty()) {
      return () -> "Error: Property cannot be empty.";
    }
    if (newValue == null || newValue.trim().isEmpty()) {
      return () -> "Error: New value cannot be empty.";
    }

    EditMode mode;
    try {
      mode = EditMode.valueOf(strmode.toUpperCase());
    } catch (IllegalArgumentException e) {
      return () -> "Error: Invalid EditMode: " + strmode;
    }

    LocalDateTime from = null;
    if ((mode == EditEventCommand.EditMode.SINGLE || mode == EditEventCommand.EditMode.FROM)) {
      if (strFrom == null || strFrom.trim().isEmpty()) {
        return () -> "Error: Start time is required for SINGLE or FROM mode.";
      }
      try {
        from = CommandParserStatic.parseDateTimeStatic(strFrom);
      } catch (InvalidDateException e) {
        return () -> "Error parsing start time: " + e.getMessage();
      }
    }

    return new EditEventCommand(service, subject, from, property, newValue, mode, true);
  }
}
