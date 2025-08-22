package model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * A utility class that handles timezone migration for events in a calendar context.
 */
public class EventMigrationHelper {

  /**
   * Migrates all events in the given calendar from an old timezone to a new timezone.
   *
   * @param calendar the calendar context containing the events to migrate
   * @param oldZone  the original timezone of the events
   * @param newZone  the new timezone to which events should be migrated
   * @throws Exception if there is an issue retrieving or updating the events
   */
  public static void migrateEvents(ICalendarContext calendar, ZoneId oldZone, ZoneId newZone)
      throws Exception {
    List<Event> events = calendar.getCalendarService().getAllEvents();
    for (Event e : events) {
      if (e instanceof SingleEvent) {
        SingleEvent se = (SingleEvent) e;
        ZonedDateTime oldStartZDT = se.getStartDateTime().atZone(oldZone);
        ZonedDateTime oldEndZDT = se.getEffectiveEndDateTime().atZone(oldZone);
        ZonedDateTime newStartZDT = oldStartZDT.withZoneSameInstant(newZone);
        ZonedDateTime newEndZDT = oldEndZDT.withZoneSameInstant(newZone);
        se.setStartDateTime(newStartZDT.toLocalDateTime());
        se.setEndDateTime(newEndZDT.toLocalDateTime());
      } else if (e instanceof RecurringEvent) {
        RecurringEvent re = (RecurringEvent) e;
        ZonedDateTime oldSeriesStartZDT = re.getStartDateTime().atZone(oldZone);
        ZonedDateTime oldSeriesEndZDT = re.getEffectiveEndDateTime().atZone(oldZone);
        ZonedDateTime newSeriesStartZDT = oldSeriesStartZDT.withZoneSameInstant(newZone);
        ZonedDateTime newSeriesEndZDT = oldSeriesEndZDT.withZoneSameInstant(newZone);
        re.setStartDateTime(newSeriesStartZDT.toLocalDateTime());
        re.setEndDateTime(newSeriesEndZDT.toLocalDateTime());
        if (re.getRecurrenceEndDate() != null) {
          ZonedDateTime oldRecurrenceZDT = re.getRecurrenceEndDate().atStartOfDay().atZone(oldZone);
          ZonedDateTime newRecurrenceZDT = oldRecurrenceZDT.withZoneSameInstant(newZone);
          re.setRecurrenceEndDate(newRecurrenceZDT.toLocalDate());
        }
      }
    }
  }
}
