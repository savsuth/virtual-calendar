package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.Test;

/**
 * JUnit Test Cases for EventCopyHelper class.
 */
public class EventCopyHelperTest {

  @Test
  public void testCopySingleEvent() throws Exception {
    CalendarContext sourceContext = new
        CalendarContext("Source", "America/New_York");
    CalendarContext targetContext = new CalendarContext("Target", "Asia/Kolkata");
    sourceContext.getCalendarService().addSingleEvent("CopyTest",
        LocalDateTime.of(2025, 1, 1, 9, 0),
        LocalDateTime.of(2025, 1, 1, 10, 0),
        "Test description", "Office", true, true);
    LocalDateTime targetStart = LocalDateTime.of(2025, 1, 1, 19,
        30);
    String result = EventCopyHelper.copyEvent(sourceContext, targetContext, "CopyTest",
        LocalDateTime.of(2025, 1, 1, 9, 0), targetStart);
    assertTrue(result.contains("copied to calendar"));
    List<Event> targetEvents = targetContext.getCalendarService().getAllEvents();
    boolean found = false;
    for (Event e : targetEvents) {
      if (e.getSubject().equals("CopyTest") &&
          e.getStartDateTime().equals(targetStart)) {
        found = true;
        break;
      }
    }
    assertTrue(found);
  }

  @Test
  public void testCopyRecurringEventWithEndDate() throws Exception {

    CalendarContext sourceContext = new CalendarContext("SourceCal", "America/New_York");
    CalendarContext targetContext = new CalendarContext("TargetCal", "Europe/London");
    sourceContext.getCalendarService().addRecurringEvent(
        "RecEvent",
        LocalDateTime.of(2025, 3, 10, 9, 0),
        LocalDateTime.of(2025, 3, 10, 10, 0),
        "desc",
        "loc",
        true,
        java.util.Collections.singleton(java.time.DayOfWeek.MONDAY),
        4,
        LocalDate.of(2025, 3, 31),
        false
    );

    LocalDateTime sourceOccurrenceStart = LocalDateTime.of(2025, 3, 17, 9, 0);
    LocalDateTime targetStart = LocalDateTime.of(2025, 3, 17, 14, 0);

    String result = EventCopyHelper.copyRecurringEvent(
        sourceContext, targetContext, "RecEvent", sourceOccurrenceStart, targetStart);
    assertTrue(result.contains("copied to calendar 'TargetCal' starting at"));

    List<Event> targetEvents = targetContext.getCalendarService().getAllEvents();
    assertFalse("Expected at least one event in target calendar.", targetEvents.isEmpty());

    boolean foundRecCopy = false;
    for (Event e : targetEvents) {
      if (e.getSubject().equalsIgnoreCase("RecEvent")) {
        foundRecCopy = true;
        break;
      }
    }
    assertTrue("Copied recurring event should be found in target calendar.", foundRecCopy);
  }

  @Test
  public void testCopyRecurringEventNoEndDate() throws Exception {

    CalendarContext sourceContext = new CalendarContext("NoEndDateSrc", "America/Chicago");
    CalendarContext targetContext = new CalendarContext("NoEndDateTgt", "Asia/Tokyo");

    sourceContext.getCalendarService().addRecurringEvent(
        "NoEnd",
        LocalDateTime.of(2025, 4, 10, 9, 0),
        LocalDateTime.of(2025, 4, 10, 10, 0),
        "desc",
        "location",
        true,
        java.util.Collections.singleton(java.time.DayOfWeek.THURSDAY),
        2,
        null,
        false
    );

    LocalDateTime occurrenceStart = LocalDateTime.of(2025, 4, 10, 9, 0);
    LocalDateTime targetStart = LocalDateTime.of(2025, 4, 10, 20, 0);
    String msg = EventCopyHelper.copyRecurringEvent(
        sourceContext, targetContext, "NoEnd", occurrenceStart, targetStart);
    assertTrue(msg.contains("copied to calendar"));
  }

  @Test
  public void testCopyEventsOnDateNoEvents() throws Exception {

    CalendarContext sourceContext = new CalendarContext("Src", "America/New_York");
    CalendarContext targetContext = new CalendarContext("Tgt", "America/New_York");

    String dateResult = EventCopyHelper.copyEventsOnDate(
        sourceContext, targetContext, LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 2));
    assertTrue(dateResult.contains("No events found on 2025-07-01"));
  }

  @Test
  public void testCopyEventsBetweenDatesEmptyResult() throws Exception {

    CalendarContext sourceContext = new CalendarContext("BtwSrc", "America/Denver");
    CalendarContext targetContext = new CalendarContext("BtwTgt", "America/Denver");

    sourceContext.getCalendarService().addSingleEvent(
        "OutsideRange",
        LocalDateTime.of(2025, 8, 15, 10, 0),
        LocalDateTime.of(2025, 8, 15, 11, 0),
        "desc", "office", true, true);

    String result = EventCopyHelper.copyEventsBetweenDates(
        sourceContext, targetContext,
        LocalDate.of(2025, 8, 1),
        LocalDate.of(2025, 8, 5),
        LocalDate.of(2025, 9, 1));
    assertTrue(result.contains("No events found between 2025-08-01 and 2025-08-05"));
  }

}

