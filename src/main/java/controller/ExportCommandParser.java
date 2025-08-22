package controller;

import model.ICalendarService;

/**
 * Parses export commands and creates the corresponding export command. Handles input for exporting
 * calendar data to a file.
 */
public class ExportCommandParser implements ICommandParser {

  private ICalendarService calendarService;

  /**
   * Constructs a parser with the given calendar service.
   *
   * @param calendarService The service managing the calendar
   */
  public ExportCommandParser(ICalendarService calendarService) {
    this.calendarService = calendarService;
  }

  /**
   * Parses the provided tokens to create an export command.
   *
   * @param tokens The array of strings from the command input
   * @return A Command object for exporting
   */
  @Override
  public Command parse(String[] tokens) {
    try {
      if (tokens.length >= 3 && tokens[1].equalsIgnoreCase("cal")) {
        String fileName = tokens[2];
        String format = "csv";  // Default format
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
          format = fileName.substring(dotIndex + 1).toLowerCase();
        }
        return new ExportCalendarCommand(calendarService, format, fileName);
      } else {
        return () -> "Invalid export command.";
      }
    } catch (Exception e) {
      return () -> "Error processing export command: " + e.getMessage();
    }
  }
}