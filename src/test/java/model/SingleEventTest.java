package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;


/**
 * JUnit Test Case for {@link SingleEvent}.
 */
public class SingleEventTest {

  @Test
  public void testValidSingleEvent() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event = new SingleEvent("Meeting", start, end, "Desc", "Room 101", true);
    assertEquals("Meeting", event.getSubject());
    assertEquals(start, event.getStartDateTime());
    assertEquals(end, event.getEffectiveEndDateTime());
  }

  @Test(expected = InvalidDateException.class)
  public void testInvalidSingleEventEndBeforeStart() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 9, 0);
    new SingleEvent("Invalid", start, end, "", "", true);
  }

  @Test
  public void testAllDayEventEffectiveEndTime() throws InvalidDateException {
    LocalDateTime start = LocalDate.of(2025, 3, 1).atStartOfDay();
    SingleEvent event = new SingleEvent("AllDay", start, null, "", "", true);
    LocalDateTime expectedEnd = LocalDate.of(2025, 3, 1).atTime(23, 59);
    assertEquals(expectedEnd, event.getEffectiveEndDateTime());
  }

  @Test
  public void testConflictsBetweenSingleEvents() throws InvalidDateException {
    LocalDateTime start1 = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end1 = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event1 = new SingleEvent("Event1", start1, end1, "", "", true);

    LocalDateTime start2 = LocalDateTime.of(2025, 3, 1, 9, 30);
    LocalDateTime end2 = LocalDateTime.of(2025, 3, 1, 10, 30);
    SingleEvent event2 = new SingleEvent("Event2", start2, end2, "", "", true);

    assertTrue(event1.conflictsWith(event2));
    assertTrue(event2.conflictsWith(event1));
  }

  @Test
  public void testNonConflictingSingleEvents() throws InvalidDateException {
    LocalDateTime start1 = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end1 = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event1 = new SingleEvent("Event1", start1, end1, "", "", true);

    LocalDateTime start2 = LocalDateTime.of(2025, 3, 1, 10, 0);
    LocalDateTime end2 = LocalDateTime.of(2025, 3, 1, 11, 0);
    SingleEvent event2 = new SingleEvent("Event2", start2, end2, "", "", true);

    assertFalse(event1.conflictsWith(event2));
    assertFalse(event2.conflictsWith(event1));
  }

  @Test
  public void testConflictWithRecurringEventUsingSingleEvent() throws InvalidDateException {
    LocalDateTime singleStart = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime singleEnd = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent single = new SingleEvent("Test", singleStart, singleEnd, "", "", true);

    LocalDateTime recurStart = LocalDateTime.of(2025, 3, 1, 9, 30);
    LocalDateTime recurEnd = LocalDateTime.of(2025, 3, 1, 9, 45);
    Set<DayOfWeek> days = new HashSet<>();
    days.add(recurStart.getDayOfWeek());
    RecurringEvent recurring = new RecurringEvent("TestRecurring", recurStart, recurEnd, "", "",
        true, days, 1, null);

    assertTrue(single.conflictsWith(recurring));
  }

  @Test
  public void testGetOccurrencesReturnsSelf() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event = new SingleEvent("Test", start, end, "Desc", "Room101", true);
    assertEquals(1, event.getOccurrences().size());
    assertSame(event, event.getOccurrences().get(0));
  }

}