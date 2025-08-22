package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

/**
 * Provides calendar services that support managing multiple calendars and delegating event
 * operations.
 */
public class MultiCalendarService implements IMultiCalendarService {

  private ICalendarManager calendarManager;

  /**
   * Constructs a MultiCalendarService with the given calendar manager.
   *
   * @param calendarManager the calendar manager used to handle calendar contexts
   */
  public MultiCalendarService(ICalendarManager calendarManager) {
    this.calendarManager = calendarManager;
  }

  @Override
  public boolean createCalendar(String name, String timezone) throws Exception {
    try {
      ZoneId.of(timezone);
    } catch (Exception e) {
      throw new Exception("Invalid timezone: " + timezone);
    }
    if (calendarManager.getCalendar(name) != null) {
      return false;
    }
    ICalendarContext cal = new CalendarContext(name, timezone);
    return calendarManager.addCalendar(cal);
  }

  @Override
  public boolean editCalendar(String calendarName, String property, String newValue)
      throws Exception {
    ICalendarContext cal = calendarManager.getCalendar(calendarName);
    if (cal == null) {
      return false;
    }
    if (property.equalsIgnoreCase("name")) {
      if (calendarManager.getCalendar(newValue) != null) {
        return false;
      }
      calendarManager.removeCalendar(calendarName);
      cal.setName(newValue);
      calendarManager.addCalendar(cal);
      if (calendarManager.getCurrentCalendar() != null &&
          calendarManager.getCurrentCalendar().getName().equals(calendarName)) {
        calendarManager.setCurrentCalendar(newValue);
      }
      return true;
    } else if (property.equalsIgnoreCase("timezone")) {
      ZoneId oldZone = cal.getTimezone();
      ZoneId newZone;
      try {
        newZone = ZoneId.of(newValue);
      } catch (Exception e) {
        throw new Exception("Invalid timezone: " + newValue);
      }
      cal.setTimezone(newValue);
      EventMigrationHelper.migrateEvents(cal, oldZone, newZone);
      return true;
    }
    return false;
  }

  @Override
  public boolean useCalendar(String calendarName) throws Exception {
    ICalendarContext cal = calendarManager.getCalendar(calendarName);
    if (cal == null) {
      return false;
    }
    calendarManager.setCurrentCalendar(calendarName);
    return true;
  }

  private ICalendarContext getCurrentCalendar() throws Exception {
    ICalendarContext cal = calendarManager.getCurrentCalendar();
    if (cal == null) {
      throw new Exception("No active calendar set.");
    }
    return cal;
  }

  @Override
  public void addSingleEvent(String subject, LocalDateTime start, LocalDateTime end,
      String description, String location, boolean isPublic,
      boolean autoDecline) throws Exception {
    getCurrentCalendar().getCalendarService()
        .addSingleEvent(subject, start, end, description, location, isPublic, true);
  }

  @Override
  public void addRecurringEvent(String subject, LocalDateTime start, LocalDateTime end,
      String description, String location, boolean isPublic,
      Set<DayOfWeek> recurrenceDays,
      int occurrenceCount, LocalDate recurrenceEndDate,
      boolean autoDecline) throws Exception {
    getCurrentCalendar().getCalendarService()
        .addRecurringEvent(subject, start, end, description, location,
            isPublic, recurrenceDays, occurrenceCount, recurrenceEndDate, true);
  }

  @Override
  public List<Event> getEventsOn(LocalDate date) {
    try {
      return getCurrentCalendar().getCalendarService().getEventsOn(date);
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public List<Event> getAllEvents() {
    try {
      return getCurrentCalendar().getCalendarService().getAllEvents();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public boolean isBusyAt(LocalDateTime dateTime) {
    try {
      return getCurrentCalendar().getCalendarService().isBusyAt(dateTime);
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public void editEvent(String subject, LocalDateTime from, String property,
      String newValue, EditMode mode) throws Exception {
    getCurrentCalendar().getCalendarService().editEvent(subject, from, property, newValue, mode);
  }

  @Override
  public String exportTo(String format, String path) throws Exception {
    return getCurrentCalendar().getCalendarService().exportTo(format, path);
  }

  @Override
  public String importFrom(String format, String path) throws Exception {
    return getCurrentCalendar().getCalendarService().importFrom(format, path);
  }

  @Override
  public String printEventsOn(LocalDate date) throws Exception {
    return getCurrentCalendar().getCalendarService().printEventsOn(date);
  }

  @Override
  public String printEventsRange(LocalDateTime start, LocalDateTime end) throws Exception {
    return getCurrentCalendar().getCalendarService().printEventsRange(start, end);
  }

  @Override
  public String copyEvent(String eventName, LocalDateTime sourceStart,
      String targetCalendarName, LocalDateTime targetStart) throws Exception {
    ICalendarContext sourceCal = getCurrentCalendar();
    ICalendarContext targetCal = calendarManager.getCalendar(targetCalendarName);
    if (targetCal == null) {
      throw new Exception("Target calendar not found: " + targetCalendarName);
    }
    return EventCopyHelper.copyEvent(sourceCal, targetCal, eventName, sourceStart, targetStart);
  }

  @Override
  public String copyEventsOn(LocalDate sourceDate, String targetCalendarName,
      LocalDate targetDate) throws Exception {
    ICalendarContext sourceCal = getCurrentCalendar();
    ICalendarContext targetCal = calendarManager.getCalendar(targetCalendarName);
    if (targetCal == null) {
      throw new Exception("Target calendar not found: " + targetCalendarName);
    }
    return EventCopyHelper.copyEventsOnDate(sourceCal, targetCal, sourceDate, targetDate);
  }

  @Override
  public String copyEventsBetween(LocalDate sourceStartDate, LocalDate sourceEndDate,
      String targetCalendarName, LocalDate targetStartDate)
      throws Exception {
    ICalendarContext sourceCal = getCurrentCalendar();
    ICalendarContext targetCal = calendarManager.getCalendar(targetCalendarName);
    if (targetCal == null) {
      throw new Exception("Target calendar not found: " + targetCalendarName);
    }
    return EventCopyHelper.copyEventsBetweenDates(sourceCal, targetCal, sourceStartDate,
        sourceEndDate, targetStartDate);
  }

  @Override
  public String[] getCurrentCalendarNameAndZone() throws Exception {
    ICalendarContext currentCal = getCurrentCalendar();
    String name = currentCal.getName();
    String timezone = currentCal.getTimezone().toString();
    return new String[]{name, timezone};
  }
}
