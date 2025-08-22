package controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;
import model.IMultiCalendarService;
import model.InvalidDateException;

/**
 * Parses GUI input to create a recurring calendar event.
 */
public class GUICreateRecurringEventParser {

  private static final DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private IMultiCalendarService service;

  /**
   * Constructs a parser for creating recurring events.
   *
   * @param service the calendar service
   */
  public GUICreateRecurringEventParser(IMultiCalendarService service) {
    this.service = service;
  }

  /**
   * Parses parameters to create a recurring event command.
   *
   * @param subject             the event subject
   * @param start               the start datetime string
   * @param end                 the end datetime string
   * @param description         the event description
   * @param location            the event location
   * @param isPublic            whether the event is public
   * @param strRecurrenceDays   days of recurrence (e.g., "MWF")
   * @param occurrenceCount     how many times the event should recur
   * @param strRecurrenceEndDate end date of recurrence
   * @param autoDecline         whether to auto-decline conflicts
   * @return a command to create the recurring event
   */
  public Command parse(String subject, String start, String end, String description,
      String location, boolean isPublic, String strRecurrenceDays,
      String occurrenceCount, String strRecurrenceEndDate, boolean autoDecline) {
    try {
      if (subject == null || subject.trim().isEmpty()) {
        return () -> "Error: Subject cannot be empty.";
      }
      if (start == null || start.trim().isEmpty()) {
        return () -> "Error: Start time is required.";
      }

      if (strRecurrenceDays == null || strRecurrenceDays.trim().isEmpty()) {
        return () -> "Error: Recurrence days are required.";
      }

      LocalDateTime ldstart;
      try {
        ldstart = CommandParserStatic.parseDateTimeStatic(start);
      } catch (InvalidDateException e) {
        return () -> "Error parsing start date/time: " + e.getMessage();
      }

      LocalDateTime ldend;
      if (end == null || end.trim().isEmpty()) {
        ldend = ldstart.toLocalDate().atTime(23, 59, 59);
      } else {
        if (end.contains("T")) {
          try {
            ldend = CommandParserStatic.parseDateTimeStatic(end);
          } catch (InvalidDateException e) {
            return () -> "Error parsing end date/time: " + e.getMessage();
          }
        } else {
          try {
            LocalDate date = LocalDate.parse(end, DATE_FORMAT);
            ldend = date.atTime(23, 59);
          } catch (Exception e) {
            return () -> "Error parsing end date: " + e.getMessage();
          }
        }
      }

      Set<DayOfWeek> recurrenceDays = parseWeekday(strRecurrenceDays);
      boolean hasOccurrenceCount = occurrenceCount != null && !occurrenceCount.trim().isEmpty();
      boolean hasRecurrenceEnd =
          strRecurrenceEndDate != null && !strRecurrenceEndDate.trim().isEmpty();
      if (!hasOccurrenceCount && !hasRecurrenceEnd) {
        return () -> "Error: Either occurrence count or recurrence end date must be provided.";
      }

      int occCount;
      if (hasOccurrenceCount) {
        try {
          occCount = Integer.parseInt(occurrenceCount.trim());
          if (occCount < 1) {
            return () -> "Error: Occurrence count must be at least 1.";
          }
        } catch (NumberFormatException e) {
          return () -> "Error: Occurrence count must be numeric.";
        }
      } else {
        occCount = -1;
      }

      LocalDate recurrenceEndDate = null;
      if (hasRecurrenceEnd) {
        try {
          recurrenceEndDate = LocalDate.parse(strRecurrenceEndDate.trim(), DATE_FORMAT);
        } catch (DateTimeParseException e) {
          return () -> "Error: Recurrence end date must be in the format yyyy-MM-dd.";
        }
      }
      return new CreateRecurringEventCommand(service, autoDecline, subject, ldstart, ldend,
          description, location, isPublic, recurrenceDays, occCount,
          recurrenceEndDate);

    } catch (Exception e) {
      return () -> "Error processing create recurring event command: " + e.getMessage();
    }
  }

  private Set<DayOfWeek> parseWeekday(String strRecurrenceDays) {
    Set<DayOfWeek> days = new HashSet<>();
    for (char ch : strRecurrenceDays.toCharArray()) {
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
