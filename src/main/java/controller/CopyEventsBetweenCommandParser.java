package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import model.IMultiCalendarService;

/**
 * Parses command tokens to create a CopyEventsBetweenCommand. Handles parsing for bulk event
 * copying between date ranges across calendars.
 */
public class CopyEventsBetweenCommandParser implements ICommandParser {

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private IMultiCalendarService multiCalendarService;

  /**
   * Constructor for the above class.
   *
   * @param multiCalendarService The service for managing calendar operations
   */
  public CopyEventsBetweenCommandParser(IMultiCalendarService multiCalendarService) {
    this.multiCalendarService = multiCalendarService;
  }

  /**
   * Parses an array of tokens into a CopyEventsBetweenCommand.
   *
   * @param tokens The array of strings from the command input
   * @return A Command object or command error
   */
  @Override
  public Command parse(String[] tokens) {
    try {
      if (tokens.length != 10) {
        return () -> "Invalid copy events between command format.";
      }
      if (!tokens[1].equalsIgnoreCase("events")
          || !tokens[2].equalsIgnoreCase("between")) {
        return () -> "Invalid copy events between command syntax.";
      }
      LocalDate sourceStart = LocalDate.parse(tokens[3], DATE_FORMAT);
      if (!tokens[4].equalsIgnoreCase("and")) {
        return () -> "Missing 'and' in copy events between command.";
      }
      LocalDate sourceEnd = LocalDate.parse(tokens[5], DATE_FORMAT);
      if (!tokens[6].equalsIgnoreCase("--target")) {
        return () -> "Missing '--target' in copy events between command.";
      }
      String targetCalendarName = tokens[7];
      if (!tokens[8].equalsIgnoreCase("to")) {
        return () -> "Missing 'to' keyword in copy events between command.";
      }
      LocalDate targetStart = LocalDate.parse(tokens[9], DATE_FORMAT);
      return new CopyEventsBetweenCommand(multiCalendarService, sourceStart,
          sourceEnd, targetCalendarName, targetStart);
    } catch (Exception e) {
      return () -> "Error processing copy events between command: " + e.getMessage();
    }
  }
}
