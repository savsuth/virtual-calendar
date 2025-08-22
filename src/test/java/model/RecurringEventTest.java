package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;

/**
 * JUnit Test Case for {@link RecurringEvent}.
 */
public class RecurringEventTest {

  @Test
  public void testGenerateOccurrencesFixedCount() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 3, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 3, 9, 15);
    Set<DayOfWeek> days = new HashSet<>();
    days.add(DayOfWeek.MONDAY);

    RecurringEvent re = new RecurringEvent("Standup", start, end, "", "", true, days, 3, null);
    List<SingleEvent> occurrences = re.generateOccurrences();
    assertEquals("There should be 3 occurrences.", 3, occurrences.size());
    for (SingleEvent occurrence : occurrences) {
      assertEquals("Occurrence should be on Monday.", DayOfWeek.MONDAY,
          occurrence.getStartDateTime().getDayOfWeek());
    }
  }

  @Test
  public void testGenerateOccurrencesUntilDate() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 3, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 3, 9, 15);
    Set<DayOfWeek> days = new HashSet<>();
    days.add(DayOfWeek.MONDAY);

    LocalDate until = LocalDate.of(2025, 3, 17);
    RecurringEvent re = new RecurringEvent("WeeklyMeeting", start, end, "", "", true, days, -1,
        until);
    List<SingleEvent> occurrences = re.generateOccurrences();

    assertEquals("Expected 3 occurrences using recurrence end date.", 3, occurrences.size());
  }

  @Test(expected = InvalidDateException.class)
  public void testInvalidRecurringEventDifferentDay() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 3, 9, 0);

    LocalDateTime end = LocalDateTime.of(2025, 3, 4, 9, 15);
    Set<DayOfWeek> days = new HashSet<>();
    days.add(DayOfWeek.MONDAY);
    new RecurringEvent("Invalid", start, end, "", "", true, days, 3, null);
  }

  @Test
  public void testRecurringEventConflict() throws InvalidDateException {
    LocalDateTime startRecurring = LocalDateTime.of(2025, 3, 3, 9, 0);
    LocalDateTime endRecurring = LocalDateTime.of(2025, 3, 3, 9, 15);
    Set<DayOfWeek> days = new HashSet<>();
    days.add(DayOfWeek.MONDAY);
    RecurringEvent recurring = new RecurringEvent("Standup", startRecurring, endRecurring, "", "",
        true, days, 3, null);

    LocalDateTime startSingle = LocalDateTime.of(2025, 3, 3, 9, 5);
    LocalDateTime endSingle = LocalDateTime.of(2025, 3, 3, 9, 10);
    try {
      SingleEvent single = new SingleEvent("Overlap", startSingle, endSingle, "", "", true);
      assertTrue("Recurring event should conflict with overlapping single event.",
          recurring.conflictsWith(single));
    } catch (InvalidDateException e) {
      fail("No exception expected: " + e.getMessage());
    }
  }

  @Test
  public void testConflictsWithNoConflict() throws InvalidDateException {
    LocalDateTime startRecurring = LocalDateTime.of(2025, 3, 3, 9, 0);
    LocalDateTime endRecurring = LocalDateTime.of(2025, 3, 3, 9, 15);
    Set<DayOfWeek> days = new HashSet<>();
    days.add(DayOfWeek.MONDAY);
    RecurringEvent recurring = new RecurringEvent("Standup", startRecurring, endRecurring,
        "Daily meeting", "Room1", true, days, 2, null);

    LocalDateTime singleStart = LocalDateTime.of(2025, 3, 3, 10, 0);
    LocalDateTime singleEnd = LocalDateTime.of(2025, 3, 3, 10, 30);
    try {
      SingleEvent single = new SingleEvent("OtherEvent", singleStart, singleEnd, "", "", true);
      assertFalse("There should be no conflict between non-overlapping events.",
          recurring.conflictsWith(single));
    } catch (InvalidDateException e) {
      fail("No exception expected: " + e.getMessage());
    }
  }

  @Test
  public void testGetOccurrencesReturnsExpectedList() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 3, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 3, 9, 15);
    Set<DayOfWeek> days = new HashSet<>();
    days.add(DayOfWeek.MONDAY);
    RecurringEvent recurring = new RecurringEvent("Standup", start, end, "Daily meeting", "Room1",
        true, days, 3, null);
    List<Event> occs = recurring.getOccurrences();
    assertNotNull("Occurrences list should not be null.", occs);
    assertEquals("Expected 3 occurrences from getOccurrences.", 3, occs.size());
  }

  @Test
  public void testGenerateOccurrencesWithRecurrenceEndDate() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 3, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 3, 9, 15);
    Set<DayOfWeek> days = new HashSet<>();
    days.add(DayOfWeek.MONDAY);
    LocalDate recurrenceEndDate = LocalDate.of(2025, 3, 10);
    RecurringEvent recurring = new RecurringEvent("Standup", start, end, "Daily meeting", "Room1",
        true, days, -1, recurrenceEndDate);
    List<SingleEvent> occurrences = recurring.generateOccurrences();

    assertEquals("Expected 2 occurrences based on recurrence end date.", 2, occurrences.size());
    assertEquals("The second occurrence should occur on the recurrence end date.",
        recurrenceEndDate, occurrences.get(1).getStartDateTime().toLocalDate());
  }


  @Test
  public void getRecurrenceDays() throws InvalidDateException {

    Set<DayOfWeek> expectedDays = new HashSet<>();
    expectedDays.add(DayOfWeek.MONDAY);
    expectedDays.add(DayOfWeek.WEDNESDAY);
    LocalDateTime start = LocalDateTime.of(2025, 3, 3, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 3, 9, 15);
    RecurringEvent recurring = new RecurringEvent("Standup", start, end, "Meeting", "Room1", true,
        expectedDays, 2, null);

    assertEquals("Recurrence days should match the provided set.", expectedDays,
        recurring.getRecurrenceDays());
  }

  @Test
  public void getOccurrenceCount() throws InvalidDateException {
    int occCount = 5;
    Set<DayOfWeek> days = new HashSet<>();
    days.add(DayOfWeek.FRIDAY);
    LocalDateTime start = LocalDateTime.of(2025, 4, 4, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 4, 4, 11, 0);
    RecurringEvent recurring = new RecurringEvent("Friday Meeting", start, end, "Discussion",
        "RoomF", true, days, occCount, null);

    assertEquals("Occurrence count should match the provided value.", occCount,
        recurring.getOccurrenceCount());
  }

  @Test
  public void getRecurrenceEndDate() throws InvalidDateException {
    LocalDate expectedEndDate = LocalDate.of(2025, 5, 1);
    Set<DayOfWeek> days = new HashSet<>();
    days.add(DayOfWeek.TUESDAY);
    LocalDateTime start = LocalDateTime.of(2025, 4, 1, 14, 0);
    LocalDateTime end = LocalDateTime.of(2025, 4, 1, 15, 0);
    RecurringEvent recurring = new RecurringEvent("Tuesday Sync", start, end, "Sync meeting",
        "RoomT", true, days, -1, expectedEndDate);

    assertEquals("Recurrence end date should match the provided date.", expectedEndDate,
        recurring.getRecurrenceEndDate());
  }

  @Test
  public void setRecurrenceEndDate() throws InvalidDateException {
    LocalDate initialEnd = LocalDate.of(2025, 5, 1);
    Set<DayOfWeek> days = new HashSet<>();
    days.add(DayOfWeek.THURSDAY);
    LocalDateTime start = LocalDateTime.of(2025, 4, 2, 16, 0);
    LocalDateTime end = LocalDateTime.of(2025, 4, 2, 17, 0);
    RecurringEvent recurring = new RecurringEvent("Thursday Check", start, end, "Check meeting",
        "RoomTh", true, days, 4, initialEnd);

    assertEquals("Initial recurrence end date should match.", initialEnd,
        recurring.getRecurrenceEndDate());

    LocalDate newEnd = LocalDate.of(2025, 5, 15);
    recurring.setRecurrenceEndDate(newEnd);
    assertEquals("Recurrence end date should be updated to the new value.", newEnd,
        recurring.getRecurrenceEndDate());
  }

}