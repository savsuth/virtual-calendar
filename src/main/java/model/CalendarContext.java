package model;

import java.time.ZoneId;

/**
 * Manages a single calendar's context, including its name, timezone, and associated services.
 */
public class CalendarContext implements ICalendarContext {

  private String name;
  private ZoneId timezone;
  private ICalendarModel calendarModel;
  private ICalendarService calendarService;

  /**
   * Creates a new calendar context with a name and timezone.
   *
   * @param name     The name of the calendar
   * @param timezone The timezone for the calendar
   */
  public CalendarContext(String name, String timezone) {
    this.name = name;
    this.timezone = ZoneId.of(timezone);
    this.calendarModel = new CalendarModel();
    this.calendarService = new CalendarService(this.calendarModel);
  }

  /**
   * Gets the name of the calendar.
   *
   * @return The calendar’s name
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Updates the name of the calendar.
   *
   * @param newName The new name to set
   */
  @Override
  public void setName(String newName) {
    this.name = newName;
  }

  /**
   * Gets the timezone of the calendar.
   *
   * @return The calendar’s timezone
   */
  @Override
  public ZoneId getTimezone() {
    return timezone;
  }

  /**
   * Updates the timezone of the calendar.
   *
   * @param timezone The new timezone to set
   */
  @Override
  public void setTimezone(String timezone) {
    this.timezone = ZoneId.of(timezone);
  }

  /**
   * Gets the calendar model associated with this context.
   *
   * @return The calendar model instance
   */
  @Override
  public ICalendarModel getCalendarModel() {
    return calendarModel;
  }

  /**
   * Gets the calendar service associated with this context.
   *
   * @return The calendar service instance
   */
  @Override
  public ICalendarService getCalendarService() {
    return calendarService;
  }
}