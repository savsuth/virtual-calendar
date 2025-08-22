package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import model.ICalendarService;

/**
 * Parses print commands to display events for a specific date or range.
 */
public class PrintCommandParser implements ICommandParser {

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private ICalendarService calendar;

  /**
   * Constructs a parser with the given calendar service.
   *
   * @param calendar The service managing calendar events
   */
  public PrintCommandParser(ICalendarService calendar) {
    this.calendar = calendar;
  }

  /**
   * Parses tokens to create a print command for events.
   *
   * @param tokens The array of command tokens
   * @return A Command instance for printing events, or an error command if invalid
   */
  @Override
  public Command parse(String[] tokens) {
    try {
      int index = 1;
      if (tokens.length >= 4 && tokens[index].equalsIgnoreCase("events")
          && tokens[index + 1].equalsIgnoreCase("on")) {
        String dateStr = tokens[index + 2];
        LocalDate date = LocalDate.parse(dateStr, DATE_FORMAT);
        return new PrintEventsCommand(calendar, date);
      } else if (tokens.length >= 6 && tokens[index].equalsIgnoreCase("events")
          && tokens[index + 1].equalsIgnoreCase("from")) {
        String startDTStr = tokens[index + 2];
        String endDTStr = tokens[index + 4];
        return new PrintEventsRangeCommand(calendar, startDTStr, endDTStr);
      } else {
        return () -> "Invalid print command.";
      }
    } catch (DateTimeParseException e) {
      return () -> "Invalid date format. Expected 'YYYY-MM-DD'";
    } catch (Exception e) {
      return () -> "Error processing print command: " + e.getMessage();
    }
  }
}