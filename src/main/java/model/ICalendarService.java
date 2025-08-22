package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Provides operations for managing events within a calendar.
 */
public interface ICalendarService {

  /**
   * Adds a single event to the calendar.
   *
   * @param subject      the event title
   * @param start        the start date and time
   * @param end          the end date and time
   * @param description  event description
   * @param location     event location
   * @param isPublic     visibility of the event
   * @param autoDecline  whether to auto-decline conflicts
   * @throws Exception if the event cannot be added
   */
  void addSingleEvent(String subject, LocalDateTime start, LocalDateTime end,
      String description, String location, boolean isPublic, boolean autoDecline)
      throws Exception;

  /**
   * Adds a recurring event to the calendar.
   *
   * @param subject           the event title
   * @param start             the start date and time
   * @param end               the end date and time
   * @param description       event description
   * @param location          event location
   * @param isPublic          visibility of the event
   * @param recurrenceDays    recurrence days of the week
   * @param occurrenceCount   number of occurrences
   * @param recurrenceEndDate date when recurrence ends
   * @param autoDecline       whether to auto-decline conflicts
   * @throws Exception if the event cannot be added
   */
  void addRecurringEvent(String subject, LocalDateTime start, LocalDateTime end,
      String description, String location, boolean isPublic,
      java.util.Set<java.time.DayOfWeek> recurrenceDays,
      int occurrenceCount, LocalDate recurrenceEndDate, boolean autoDecline)
      throws Exception;

  /**
   * Gets all events on the specified date.
   *
   * @param date the target date
   * @return list of events
   */
  List<Event> getEventsOn(LocalDate date);

  /**
   * Gets all events in the calendar.
   *
   * @return list of events
   */
  List<Event> getAllEvents();

  /**
   * Checks if the calendar is busy at the given date and time.
   *
   * @param dateTime the target datetime
   * @return true if busy, false otherwise
   */
  boolean isBusyAt(LocalDateTime dateTime);

  /**
   * Edits an existing event.
   *
   * @param subject   event subject
   * @param from      datetime to locate event instance
   * @param property  property to edit
   * @param newValue  new value to apply
   * @param mode      edit mode
   * @throws Exception if the edit fails
   */
  void editEvent(String subject, LocalDateTime from, String property, String newValue,
      EditMode mode) throws Exception;

  /**
   * Exports the calendar to the specified format and path.
   *
   * @param format the export format
   * @param path   the output file path
   * @return export result
   * @throws Exception if export fails
   */
  String exportTo(String format, String path) throws Exception;

  /**
   * Imports calendar data from a file.
   *
   * @param format the format of the file
   * @param path   the file path
   * @return import result
   * @throws Exception if import fails
   */
  String importFrom(String format, String path) throws Exception;

  /**
   * Prints all events on the specified date.
   *
   * @param date the target date
   * @return formatted event list
   * @throws Exception if retrieval fails
   */
  String printEventsOn(LocalDate date) throws Exception;

  /**
   * Prints all events within a datetime range.
   *
   * @param start start datetime
   * @param end   end datetime
   * @return formatted event list
   * @throws Exception if retrieval fails
   */
  String printEventsRange(LocalDateTime start, LocalDateTime end) throws Exception;

  /**
   * For different EditModes.
   */
  enum EditMode {
    SINGLE, FROM, ALL
  }
}
