package controller;

import model.IMultiCalendarService;

/**
 * Parses command tokens to create a CreateCalendarCommand.
 */
public class CreateCalendarCommandParser implements ICommandParser {

  private IMultiCalendarService multiCalendarService;

  /**
   * Constructor of the class.
   *
   * @param multiCalendarService The service for managing multiple calendars
   */
  public CreateCalendarCommandParser(IMultiCalendarService multiCalendarService) {
    this.multiCalendarService = multiCalendarService;
  }

  /**
   * Parses an array of tokens into a CreateCalendarCommand.
   *
   * @param tokens The array of strings from the command input
   * @return A Command object
   */
  @Override
  public Command parse(String[] tokens) {
    try {
      if (tokens.length != 6 || !tokens[1].equalsIgnoreCase("calendar")
          || !tokens[2].equalsIgnoreCase("--name")) {
        return () -> "Invalid create calendar command.";
      }
      String calName = tokens[3];
      if (!tokens[4].equalsIgnoreCase("--timezone")) {
        return () -> "Invalid create calendar command: Missing --timezone option.";
      }
      String timezone = tokens[5];
      return new CreateCalendarCommand(multiCalendarService, calName, timezone);
    } catch (Exception e) {
      return () -> "Error processing create calendar command: " + e.getMessage();
    }
  }
}