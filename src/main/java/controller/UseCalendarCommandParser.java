package controller;

import model.IMultiCalendarService;

/**
 * Parses commands to switch the active calendar in a multi-calendar system.
 */
public class UseCalendarCommandParser implements ICommandParser {

  private IMultiCalendarService multiCalendarService;

  /**
   * Constructs a parser with the given multi-calendar service.
   *
   * @param multiCalendarService The service managing multiple calendars
   */
  public UseCalendarCommandParser(IMultiCalendarService multiCalendarService) {
    this.multiCalendarService = multiCalendarService;
  }

  /**
   * Parses tokens to create a UseCalendarCommand.
   *
   * @param tokens The array of command tokens
   * @return A Command for switching calendars, or an error command if it is invalid
   */
  @Override
  public Command parse(String[] tokens) {
    try {
      if (tokens.length != 4 || !tokens[1].equalsIgnoreCase("calendar")
          || !tokens[2].equalsIgnoreCase("--name")) {
        return () -> "Invalid use calendar command.";
      }
      String calendarName = tokens[3];
      return new UseCalendarCommand(multiCalendarService, calendarName);
    } catch (Exception e) {
      return () -> "Error processing use calendar command: " + e.getMessage();
    }
  }
}
