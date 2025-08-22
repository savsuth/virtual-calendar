package controller;

import java.time.LocalDateTime;
import model.ICalendarService;

/**
 * Parses commands to display the current status (availability) of the calendar.
 */
public class ShowStatusCommandParser implements ICommandParser {

  private ICalendarService service;

  /**
   * Constructs a parser with the given calendar service.
   *
   * @param service The service managing calendar events
   */
  public ShowStatusCommandParser(ICalendarService service) {
    this.service = service;
  }

  /**
   * Parses tokens to create a ShowStatusCommand.
   *
   * @param tokens The array of command tokens
   * @return A Command for checking status
   */
  @Override
  public Command parse(String[] tokens) {
    try {
      if (tokens.length >= 4 && tokens[1].equalsIgnoreCase("status")
          && tokens[2].equalsIgnoreCase("on")) {
        String dateTimeStr = tokens[3];
        LocalDateTime dateTime = CommandParserStatic.parseDateTimeStatic(dateTimeStr);
        return new ShowStatusCommand(service, dateTime);
      } else {
        return () -> "Invalid show command.";
      }
    } catch (Exception e) {
      return () -> "Error processing show command: " + e.getMessage();
    }
  }
}