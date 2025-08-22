package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.Test;

/**
 * Test cases for EventMigration class. Checks if the events migrated with the change in Zones.
 */
public class EventMigrationHelperTest {

  @Test
  public void testMigrateSingleEvent() throws Exception {
    CalendarContext context = new CalendarContext("TestCal", "America/New_York");
    context.getCalendarService().addSingleEvent("Meeting",
        LocalDateTime.of(2025, 1, 1, 9, 0),
        LocalDateTime.of(2025, 1, 1, 10, 0),
        "Discuss", "Room1", true, true);
    List<Event> before = context.getCalendarService().getAllEvents();
    assertFalse(before.isEmpty());
    LocalDateTime origStart = before.get(0).getStartDateTime();
    ZoneId zone = ZoneId.of("Asia/Kolkata");
    ZoneId oldZone = context.getTimezone();
    EventMigrationHelper.migrateEvents(context, oldZone, zone);
    List<Event> after = context.getCalendarService().getAllEvents();
    assertFalse(after.isEmpty());
    LocalDateTime newStart = after.get(0).getStartDateTime();
    assertNotEquals(origStart, newStart);
  }

  @Test
  public void testMigrateRecurringEvent() throws Exception {
    CalendarContext context = new CalendarContext("TestRec", "America/New_York");
    context.getCalendarService().addRecurringEvent("Recurring",
        LocalDateTime.of(2025, 1, 1, 9, 0),
        LocalDateTime.of(2025, 1, 1, 10, 0),
        "Weekly", "Office", true,
        java.util.Collections.singleton(java.time.DayOfWeek.WEDNESDAY),
        3, LocalDate.of(2025, 1, 15), true);
    List<Event> before = context.getCalendarService().getAllEvents();
    assertFalse(before.isEmpty());
    LocalDateTime origStart = before.get(0).getStartDateTime();

    ZoneId zone = ZoneId.of("Asia/Kolkata");
    ZoneId oldZone = context.getTimezone();
    EventMigrationHelper.migrateEvents(context, oldZone, zone);
    List<Event> after = context.getCalendarService().getAllEvents();
    assertFalse(after.isEmpty());
    LocalDateTime newStart = after.get(0).getStartDateTime();
    assertNotEquals(origStart, newStart);
  }
}


