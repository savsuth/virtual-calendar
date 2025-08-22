package model;

import java.time.ZoneId;

/**
 * Represents the context of a calendar, including its name, timezone, model, and services.
 */
public interface ICalendarContext {

  /**
   * Returns the name of the calendar.
   *
   * @return the calendar name
   */
  String getName();

  /**
   * Updates the name of the calendar.
   *
   * @param newName the new name to set
   */
  void setName(String newName);

  /**
   * Returns the timezone of the calendar.
   *
   * @return the calendar's timezone
   */
  ZoneId getTimezone();

  /**
   * Updates the timezone of the calendar.
   *
   * @param timezone the new timezone in string format
   */
  void setTimezone(String timezone);

  /**
   * Gets the calendar model associated with this context.
   *
   * @return the calendar model
   */
  ICalendarModel getCalendarModel();

  /**
   * Gets the service layer for managing this calendar.
   *
   * @return the calendar service
   */
  ICalendarService getCalendarService();
}

