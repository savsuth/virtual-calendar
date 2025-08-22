package controller;

import java.time.LocalDate;

/**
 * Represents a controller interface for handling GUI-based calendar operations.
 */
public interface IGUIController extends IAppController {

  /**
   * Switches the current calendar to the specified one.
   *
   * @param calendarName the calendar name
   * @return result message
   */
  String useCalendar(String calendarName);

  /**
   * Creates a new calendar with the specified name and timezone.
   *
   * @param calendarName the calendar name
   * @param timezone     the timezone
   * @return result message
   */
  String createCalendar(String calendarName, String timezone);


  /**
   * Creates a single event with the specified properties.
   *
   * @param subject      the event subject
   * @param start        start time
   * @param end          end time
   * @param description  event description
   * @param location     event location
   * @param isPublic     visibility flag
   * @param autoDecline  auto-decline flag
   * @return result message
   */
  String createSingleEvent(String subject, String start, String end,
      String description, String location, boolean isPublic, boolean autoDecline);

  /**
   * Creates a recurring event with the specified properties.
   *
   * @param subject           event subject
   * @param start             start time
   * @param end               end time
   * @param description       event description
   * @param location          event location
   * @param isPublic          visibility flag
   * @param recurrenceDays    days of recurrence
   * @param occurrenceCount   number of occurrences
   * @param recurrenceEndDate recurrence end date
   * @param autoDecline       auto-decline flag
   * @return result message
   */
  String createRecurringEvent(String subject, String start, String end,
      String description, String location, boolean isPublic,
      String recurrenceDays, String occurrenceCount, String recurrenceEndDate, boolean autoDecline);


  /**
   * Edits a property of an event.
   *
   * @param subject   event subject
   * @param from      start time of the event (used for specific modes)
   * @param property  property to edit
   * @param newValue  new value
   * @param editMode  mode of editing
   * @return result message
   */
  String editEvent(String subject, String from, String property, String newValue, String editMode);


  /**
   * Exports the current calendar to the specified file format.
   *
   * @param fileName output file name
   * @param format   export format
   * @return result message
   */
  String exportCalendar(String fileName, String format);

  /**
   * Imports a calendar from the specified file.
   *
   * @param fileName input file name
   * @return result message
   */
  String importCalendar(String fileName);

  /**
   * Prints events for the specified date.
   *
   * @param date the date
   * @return result message
   */
  String printEventsOn(LocalDate date);

  /**
   * Edits a calendar's property.
   *
   * @param calendarName the calendar name
   * @param property     property to edit
   * @param newValue     new value
   * @return result message
   */
  String editCalendar(String calendarName, String property, String newValue);

  /**
   * Gets the name and timezone of the currently active calendar.
   *
   * @return an array with the name and timezone
   * @throws Exception if retrieval fails
   */
  String[] getCurrentCalendarNameAndZone() throws Exception;

}
