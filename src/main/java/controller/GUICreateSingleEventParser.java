package controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.IMultiCalendarService;
import model.InvalidDateException;

/**
 * Parses GUI input to create a single calendar event.
 */
public class GUICreateSingleEventParser {

  private static final DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd'T'HH:mm");


  private IMultiCalendarService service;

  /**
   * Constructs a parser for creating single events.
   *
   * @param service the calendar service
   */
  public GUICreateSingleEventParser(IMultiCalendarService service) {
    this.service = service;
  }

  /**
   * Parses parameters to create a single event command.
   *
   * @param subject     the event subject
   * @param start       the start datetime string
   * @param end         the end datetime string
   * @param description the event description
   * @param location    the event location
   * @param isPublic    whether the event is public
   * @param autoDecline whether to auto-decline conflicts
   * @return a command to create the event
   */
  public Command parse(String subject, String start, String end, String description,
      String location, boolean isPublic, boolean autoDecline) {
    try {

      if (subject == null || subject.trim().isEmpty()) {
        return () -> "Error: Subject cannot be empty.";
      }
      if (start == null || start.trim().isEmpty()) {
        return () -> "Error: Start time is required.";
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

      return new CreateEventCommand(service, autoDecline, subject, ldstart, ldend, description,
          location, isPublic);


    } catch (Exception e) {
      return () -> "Error processing create single event command: " + e.getMessage();
    }
  }
}
