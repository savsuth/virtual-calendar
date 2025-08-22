package controller;

import model.IMultiCalendarService;

/**
 * Parses command tokens to create an EditCalendarCommand and handles the input for editing calendar
 * properties like name or timezone.
 */
public class EditCalendarCommandParser implements ICommandParser {

  private IMultiCalendarService multiCalendarService;

  /**
   * Constructor for the class.
   *
   * @param multiCalendarService The service for managing multiple calendars
   */
  public EditCalendarCommandParser(IMultiCalendarService multiCalendarService) {
    this.multiCalendarService = multiCalendarService;
  }

  /**
   * Parses an array of tokens into an EditCalendarCommand.
   *
   * @param tokens The array of strings from the command input
   * @return Command object either or an error command
   */
  @Override
  public Command parse(String[] tokens) {
    try {
      if (tokens.length < 6 || !tokens[1].equalsIgnoreCase("calendar")) {
        return () -> "Invalid edit calendar command.";
      }
      String calName = null;
      String property = null;
      String newValue = null;
      for (int i = 2; i < tokens.length; i++) {
        if (tokens[i].equalsIgnoreCase("--name")) {
          calName = tokens[++i];
        } else if (tokens[i].equalsIgnoreCase("--property")) {
          property = tokens[++i];
          newValue = tokens[++i];
        }
      }
      if (calName == null || property == null || newValue == null) {
        return () -> "Incomplete edit calendar command.";
      }
      return new EditCalendarCommand(multiCalendarService, calName, property, newValue);
    } catch (Exception e) {
      return () -> "Error processing edit calendar command: " + e.getMessage();
    }
  }
}
