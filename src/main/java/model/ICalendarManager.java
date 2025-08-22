package model;

/**
 * Manages multiple calendars and their active states.
 */
public interface ICalendarManager {

  /**
   * Adds a new calendar context to the system.
   *
   * @param calendar the calendar to add
   * @return true if added successfully, false otherwise
   */
  boolean addCalendar(ICalendarContext calendar);

  /**
   * Retrieves a calendar by name.
   *
   * @param name the name of the calendar
   * @return the matching calendar context, or null if not found
   */
  ICalendarContext getCalendar(String name);

  /**
   * Gets the currently active calendar context.
   *
   * @return the current calendar
   */
  ICalendarContext getCurrentCalendar();

  /**
   * Sets the active calendar by name.
   *
   * @param name the name of the calendar to set as active
   */
  void setCurrentCalendar(String name);

  /**
   * Removes a calendar from the system.
   *
   * @param calendarName the name of the calendar to remove
   */
  void removeCalendar(String calendarName);
}

