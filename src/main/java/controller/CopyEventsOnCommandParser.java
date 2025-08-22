package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import model.IMultiCalendarService;

/**
 * Parses command tokens to create a CopyEventsOnCommand and handles parsing for copying all events
 * from one specific date to another.
 */
public class CopyEventsOnCommandParser implements ICommandParser {

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private IMultiCalendarService multiCalendarService;

  /**
   * Constructor of the class.
   *
   * @param multiCalendarService The service managing calendar operations
   */
  public CopyEventsOnCommandParser(IMultiCalendarService multiCalendarService) {
    this.multiCalendarService = multiCalendarService;
  }

  /**
   * Parses an array of tokens into a CopyEventsOnCommand.
   *
   * @param tokens The array of strings from the command input
   * @return A Command object, either a valid CopyEventsOnCommand or an error command
   */
  @Override
  public Command parse(String[] tokens) {
    try {
      if (tokens.length != 8) {
        return () -> "Invalid copy events on command format.";
      }
      if (!tokens[1].equalsIgnoreCase("events")
          || !tokens[2].equalsIgnoreCase("on")) {
        return () -> "Invalid copy events on command syntax.";
      }
      LocalDate sourceDate = LocalDate.parse(tokens[3], DATE_FORMAT);
      if (!tokens[4].equalsIgnoreCase("--target")) {
        return () -> "Missing '--target' in copy events on command.";
      }
      String targetCalendarName = tokens[5];
      if (!tokens[6].equalsIgnoreCase("to")) {
        return () -> "Missing 'to' keyword in copy events on command.";
      }
      LocalDate targetDate = LocalDate.parse(tokens[7], DATE_FORMAT);
      return new CopyEventsOnCommand(multiCalendarService,
          sourceDate, targetCalendarName, targetDate);
    } catch (Exception e) {
      return () -> "Error processing copy events on command: " + e.getMessage();
    }
  }
}