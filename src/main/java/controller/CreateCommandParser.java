package controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import model.ICalendarService;

/**
 * Parses command tokens to create event creation commands. Supports both single and recurring event
 * creation with various options.
 */
public class CreateCommandParser implements ICommandParser {

  private static final DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter DATE_TIME_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
  private ICalendarService service;

  /**
   * Constructs a parser with the given calendar service.
   *
   * @param service The calendar service for managing events
   */
  public CreateCommandParser(ICalendarService service) {
    this.service = service;
  }

  /**
   * Checks if the token at the given index specifies event visibility.
   *
   * @param tokens The array of command tokens
   * @param index  The current position in the token array
   * @return True if the event should be public, false if private
   */
  private static boolean isABoolean(String[] tokens, int index) {
    boolean isPublic = true;
    if (index < tokens.length) {
      String pubToken = tokens[index];
      if (pubToken.equalsIgnoreCase("public")) {
        isPublic = true;
      } else if (pubToken.equalsIgnoreCase("private")) {
        isPublic = false;
      }
      index++;
    }
    return isPublic;
  }

  /**
   * Parses command tokens into a Command object for event creation.
   *
   * @param tokens The array of strings from the command input
   * @return A Command object or error
   */
  @Override
  public Command parse(String[] tokens) {
    try {
      boolean autoDecline = false;
      int index = 1;
      if (!tokens[index].equalsIgnoreCase("event")) {
        return () -> "Invalid create command: missing 'event' keyword.";
      }
      index++;
      if (tokens[index].equalsIgnoreCase("--autoDecline")) {
        autoDecline = true;
        index++;
      }
      String subject = tokens[index++];
      if (tokens[index].equalsIgnoreCase("from")) {
        index++;
        String startDTStr = tokens[index++];
        LocalDateTime start = CommandParserStatic.parseDateTimeStatic(startDTStr);

        if (!tokens[index].equalsIgnoreCase("to")) {
          return () -> "Expected 'to' after start time.";
        }
        index++;
        String endDTStr = tokens[index++];

        LocalDateTime end;

        if (endDTStr.contains("T")) {
          end = CommandParserStatic.parseDateTimeStatic(endDTStr);
        } else {
          LocalDate date = LocalDate.parse(endDTStr, DATE_FORMAT);
          end = date.atTime(23, 59);
        }

        if (index < tokens.length && tokens[index].equalsIgnoreCase("repeats")) {
          return getRecurringCommand(tokens, autoDecline, index, subject, start, end);
        } else {
          boolean isPublic = isABoolean(tokens, index);
          return new CreateEventCommand(service, autoDecline, subject,
              start, end, "", "", isPublic);
        }
      } else if (tokens[index].equalsIgnoreCase("on")) {
        index++;
        String dateStr = tokens[index++];
        LocalDateTime start;
        LocalDateTime end;
        if (dateStr.contains("T")) {
          LocalDateTime dt = LocalDateTime.parse(dateStr, DATE_TIME_FORMAT);
          start = dt;
          end = dt.toLocalDate().atTime(23, 59);
        } else {
          LocalDate date = LocalDate.parse(dateStr, DATE_FORMAT);
          start = date.atStartOfDay();
          end = date.atTime(23, 59);
        }
        if (index < tokens.length && tokens[index].equalsIgnoreCase("repeats")) {
          return getRecurringCommand(tokens, autoDecline, index, subject, start, end);
        } else {
          boolean isPublic = isABoolean(tokens, index);
          return new CreateEventCommand(service, autoDecline, subject,
              start, end, "", "", isPublic);
        }
      } else {
        return () -> "Invalid create command format.";
      }
    } catch (Exception e) {
      return () -> "Error processing create command: " + e.getMessage();
    }
  }

  /**
   * Creates a recurring event command.
   *
   * @param tokens      The full array of command tokens
   * @param autoDecline Whether to auto-decline conflicting events
   * @param index       The current position in the token array
   * @param subject     The event subject
   * @param start       The event start time
   * @param end         The event end time
   * @return A Command object for creating a recurring event
   */
  private Command getRecurringCommand(String[] tokens, boolean autoDecline, int index,
      String subject,
      LocalDateTime start, LocalDateTime end) {
    index++;
    String weekdaysStr = tokens[index++];
    Set<DayOfWeek> recurrenceDays = parseWeekdays(weekdaysStr);
    Integer occCount = null;
    LocalDate recurrenceEndDate = null;
    if (index < tokens.length) {
      if (tokens[index].equalsIgnoreCase("for")) {
        index++;
        occCount = Integer.parseInt(tokens[index++]);
        if (index < tokens.length && tokens[index].equalsIgnoreCase("times")) {
          index++;
        } else {
          return () -> "Expected 'times' after occurrence count.";
        }
      } else if (tokens[index].equalsIgnoreCase("until")) {
        index++;
        String untilStr = tokens[index++];
        LocalDateTime untilDT;
        try {
          untilDT = LocalDateTime.parse(untilStr, DATE_TIME_FORMAT);
        } catch (Exception e) {
          untilDT = LocalDate.parse(untilStr, DATE_FORMAT).atStartOfDay();
        }
        recurrenceEndDate = untilDT.toLocalDate();
      }
    }
    boolean isPublic = isABoolean(tokens, index);
    return new CreateRecurringEventCommand(service, autoDecline, subject, start,
        end, "", "",
        isPublic, recurrenceDays, occCount == null ? -1 : occCount, recurrenceEndDate);
  }

  /**
   * Converts a string of weekday characters into a set of DayOfWeek values.
   *
   * @param weekdaysStr A string like "MTWRF" representing weekdays
   * @return A set of DayOfWeek enums for the specified days
   * @throws IllegalArgumentException if an invalid weekday character is provided
   */
  private Set<DayOfWeek> parseWeekdays(String weekdaysStr) {
    Set<DayOfWeek> days = new HashSet<>();
    for (char ch : weekdaysStr.toCharArray()) {
      switch (Character.toUpperCase(ch)) {
        case 'M':
          days.add(DayOfWeek.MONDAY);
          break;
        case 'T':
          days.add(DayOfWeek.TUESDAY);
          break;
        case 'W':
          days.add(DayOfWeek.WEDNESDAY);
          break;
        case 'R':
          days.add(DayOfWeek.THURSDAY);
          break;
        case 'F':
          days.add(DayOfWeek.FRIDAY);
          break;
        case 'S':
          days.add(DayOfWeek.SATURDAY);
          break;
        case 'U':
          days.add(DayOfWeek.SUNDAY);
          break;
        default:
          throw new IllegalArgumentException("Invalid weekday character: " + ch);
      }
    }
    return days;
  }
}