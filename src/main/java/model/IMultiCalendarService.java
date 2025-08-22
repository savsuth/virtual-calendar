package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Extends calendar service to support multiple calendars and event copying.
 */
public interface IMultiCalendarService extends ICalendarService {

  /**
   * Creates a new calendar with the specified name and timezone.
   *
   * @param name      the name of the calendar
   * @param timezone  the timezone for the calendar
   * @return true if created successfully
   * @throws Exception if creation fails
   */
  boolean createCalendar(String name, String timezone) throws Exception;

  /**
   * Edits a property of a calendar.
   *
   * @param calendarName the calendar name
   * @param property     the property to edit
   * @param newValue     the new value
   * @return true if edit is successful
   * @throws Exception if editing fails
   */
  boolean editCalendar(String calendarName, String property, String newValue) throws Exception;

  /**
   * Switches the active calendar to the specified one.
   *
   * @param calendarName the calendar to use
   * @return true if switched successfully
   * @throws Exception if switching fails
   */
  boolean useCalendar(String calendarName) throws Exception;

  /**
   * Copies an event to another calendar and start time.
   *
   * @param eventName         the event name
   * @param sourceStart       the original start time
   * @param targetCalendarName the target calendar
   * @param targetStart       the new start time
   * @return success message or error
   * @throws Exception if copying fails
   */
  String copyEvent(String eventName, LocalDateTime sourceStart,
      String targetCalendarName, LocalDateTime targetStart) throws Exception;

  /**
   * Copies all events from one day to another in a different calendar.
   *
   * @param sourceDate         the source date
   * @param targetCalendarName the target calendar
   * @param targetDate         the target date
   * @return success message or error
   * @throws Exception if copying fails
   */
  String copyEventsOn(LocalDate sourceDate, String targetCalendarName,
      LocalDate targetDate) throws Exception;

  /**
   * Copies all events in a date range to a target date in another calendar.
   *
   * @param sourceStartDate    the start of the source range
   * @param sourceEndDate      the end of the source range
   * @param targetCalendarName the target calendar
   * @param targetStartDate    the date to start copying to
   * @return success message or error
   * @throws Exception if copying fails
   */
  String copyEventsBetween(LocalDate sourceStartDate, LocalDate sourceEndDate,
      String targetCalendarName, LocalDate targetStartDate) throws Exception;

  /**
   * Gets the name and timezone of the current calendar.
   *
   * @return array with name and timezone
   * @throws Exception if fetching fails
   */
  String[] getCurrentCalendarNameAndZone() throws Exception;
}

