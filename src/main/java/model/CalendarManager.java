package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages a collection of calendars and tracks the currently active one.
 */
public class CalendarManager implements ICalendarManager {

  private Map<String, ICalendarContext> calendars;
  private ICalendarContext currentCalendar;

  /**
   * Creates a new calendar manager with an empty set of calendars.
   */
  public CalendarManager() {
    calendars = new HashMap<>();
  }

  /**
   * Adds a new calendar to the manager.
   *
   * @param calendar The calendar context to add
   * @return True if added successfully, false if a calendar with the same name already exists
   */
  @Override
  public boolean addCalendar(ICalendarContext calendar) {
    if (calendars.containsKey(calendar.getName())) {
      return false;
    }
    calendars.put(calendar.getName(), calendar);
    return true;
  }

  /**
   * Retrieves a calendar by its name.
   *
   * @param name The name of the calendar to get
   * @return The calendar context, or null if not found
   */
  @Override
  public ICalendarContext getCalendar(String name) {
    return calendars.get(name);
  }

  /**
   * Gets the currently active calendar.
   *
   * @return The current calendar context, or null if none is set
   */
  @Override
  public ICalendarContext getCurrentCalendar() {
    return currentCalendar;
  }

  /**
   * Sets the currently active calendar by name.
   *
   * @param name The name of the calendar to set as current
   */
  @Override
  public void setCurrentCalendar(String name) {
    if (calendars.containsKey(name)) {
      currentCalendar = calendars.get(name);
    }
  }

  /**
   * Removes a calendar from the manager by name.
   *
   * @param name The name of the calendar to remove
   */
  public void removeCalendar(String name) {
    calendars.remove(name);
    if (currentCalendar != null && currentCalendar.getName().equals(name)) {
      currentCalendar = null;
    }
  }
}