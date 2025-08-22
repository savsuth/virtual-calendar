package controller;

import java.time.LocalDateTime;
import model.IMultiCalendarService;

/**
 * Parses command tokens to create a CopyEventCommand. Handles the conversion of string-based
 * command input into a structured command object.
 */
public class CopyEventCommandParser implements ICommandParser {

  private IMultiCalendarService multiCalendarService;

  /**
   * Constructor of the class.
   *
   * @param multiCalendarService The service to manage calendar operations
   */
  public CopyEventCommandParser(IMultiCalendarService multiCalendarService) {
    this.multiCalendarService = multiCalendarService;
  }

  /**
   * Parses an array of command tokens to create a CopyEventCommand.
   *
   * @param tokens The array of strings representing the command input
   * @return A Command object, either a valid CopyEventCommand or an error command
   */
  @Override
  public Command parse(String[] tokens) {
    try {
      if (tokens.length != 9) {
        return () -> "Invalid copy event command format.";
      }
      if (!tokens[1].equalsIgnoreCase("event")) {
        return () -> "Invalid copy event command: missing 'event' keyword.";
      }
      String eventName = tokens[2];
      if (!tokens[3].equalsIgnoreCase("on")) {
        return () -> "Invalid copy event command: missing 'on' keyword.";
      }
      String sourceDateTimeStr = tokens[4];
      LocalDateTime sourceStart = CommandParserStatic.parseDateTimeStatic(sourceDateTimeStr);
      if (!tokens[5].equalsIgnoreCase("--target")) {
        return () -> "Invalid copy event command: missing '--target' option.";
      }
      String targetCalendarName = tokens[6];
      if (!tokens[7].equalsIgnoreCase("to")) {
        return () -> "Invalid copy event command: missing 'to' keyword.";
      }
      String targetDateTimeStr = tokens[8];
      LocalDateTime targetStart = CommandParserStatic.parseDateTimeStatic(targetDateTimeStr);
      return new CopyEventCommand(multiCalendarService, eventName, sourceStart,
          targetCalendarName, targetStart);
    } catch (Exception e) {
      return () -> "Error processing copy event command: " + e.getMessage();
    }
  }
}
