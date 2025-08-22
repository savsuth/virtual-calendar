package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the CalendarModel class to verify event handling and storing logic.
 */
public class CalendarModelTest {

  private CalendarModel calendar;

  @Before
  public void setUp() {
    calendar = new CalendarModel();
  }

  @Test
  public void testAddAndGetEvents() throws InvalidDateException, EventConflictException {
    LocalDateTime start1 = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end1 = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event1 = new SingleEvent("Meeting", start1, end1, "", "", true);
    calendar.addEvent(event1, false);

    LocalDate eventsDate = LocalDate.of(2025, 3, 1);
    List<Event> eventsOnDate = calendar.getEventsOn(eventsDate);
    assertEquals("There should be one event on the given date.", 1, eventsOnDate.size());
  }

  /**
   * Test that adding a conflicting event with autoDecline enabled throws an exception.
   */
  @Test
  public void testAutoDeclineConflict() throws InvalidDateException, EventConflictException {
    LocalDateTime start1 = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end1 = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event1 = new SingleEvent("Meeting", start1, end1, "", "", true);
    calendar.addEvent(event1, false);

    LocalDateTime start2 = LocalDateTime.of(2025, 3, 1, 9, 30);
    LocalDateTime end2 = LocalDateTime.of(2025, 3, 1, 10, 30);
    SingleEvent event2 = new SingleEvent("Conflict", start2, end2, "", "", true);
    event2.setAutoDecline(true);

    try {
      calendar.addEvent(event2, true);
      fail("Expected an exception due to conflict");
    } catch (Exception e) {
      String expectedMessage = "Event 'Conflict' conflicts with existing event 'Meeting'.";
      assertEquals(expectedMessage, e.getMessage());
    }
  }


  /**
   * Test that getAllEvents returns a non-empty list after adding an event.
   */
  @Test
  public void testGetAllEvents() throws InvalidDateException, EventConflictException {
    LocalDateTime start1 = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end1 = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event1 = new SingleEvent("Meeting", start1, end1, "", "", true);
    calendar.addEvent(event1, false);
    assertFalse("getAllEvents should not return an empty list after adding an event.",
        calendar.getAllEvents().isEmpty());
  }

  /**
   * Test that recurring events generate occurrences on the correct dates.
   */
  @Test
  public void testGetEventsOnForRecurringEvent()
      throws InvalidDateException, EventConflictException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 3, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 3, 9, 15);
    Set<DayOfWeek> recurrenceDays = new HashSet<>();
    recurrenceDays.add(DayOfWeek.MONDAY);
    RecurringEvent recurring = new RecurringEvent("Standup", start, end, "Daily meeting", "Room101",
        true, recurrenceDays, 2, null);
    calendar.addEvent(recurring, false);

    LocalDate date = LocalDate.of(2025, 3, 3);
    List<Event> eventsOnDate = calendar.getEventsOn(date);
    assertFalse("There should be recurring event occurrences on the given date.",
        eventsOnDate.isEmpty());
    boolean found = eventsOnDate.stream().anyMatch(e -> e.getSubject().equals("Standup"));
    assertTrue("The recurring event 'Standup' should be found on the given date.", found);
  }

  @Test
  public void addEvent() throws InvalidDateException, EventConflictException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event = new SingleEvent("Workshop", start, end, "Details", "RoomA", true);
    calendar.addEvent(event, false);
    assertTrue("Event should be added to the calendar.", calendar.getAllEvents().contains(event));
  }

  @Test
  public void getEventsOn() throws InvalidDateException, EventConflictException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 5, 14, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 5, 15, 0);
    SingleEvent event = new SingleEvent("Seminar", start, end, "Lecture", "Auditorium", true);
    calendar.addEvent(event, false);
    LocalDate date = LocalDate.of(2025, 3, 5);
    List<Event> eventsOnDate = calendar.getEventsOn(date);
    assertTrue("The event should be found on the given date.", eventsOnDate.contains(event));
  }

  @Test
  public void getAllEvents() throws InvalidDateException, EventConflictException {

    assertTrue("Calendar should be empty initially.", calendar.getAllEvents().isEmpty());
    LocalDateTime start = LocalDateTime.of(2025, 3, 7, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 7, 11, 0);
    SingleEvent event = new SingleEvent("Lecture", start, end, "Math", "RoomB", true);
    calendar.addEvent(event, false);
    List<Event> allEvents = calendar.getAllEvents();
    assertEquals("There should be exactly one event in the calendar.", 1, allEvents.size());
    assertTrue("The added event should be present in the list.", allEvents.contains(event));
  }

  @Test
  public void isBusyAt() throws InvalidDateException, EventConflictException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 10, 13, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 10, 14, 0);
    SingleEvent event = new SingleEvent("Meeting", start, end, "Team meeting", "Office", true);
    calendar.addEvent(event, false);

    LocalDateTime queryTime = LocalDateTime.of(2025, 3, 10, 13, 30);
    assertTrue("Calendar should be busy during the event.", calendar.isBusyAt(queryTime));

    LocalDateTime beforeTime = LocalDateTime.of(2025, 3, 10, 12, 0);
    assertFalse("Calendar should not be busy before the event.", calendar.isBusyAt(beforeTime));

    LocalDateTime afterTime = LocalDateTime.of(2025, 3, 10, 14, 30);
    assertFalse("Calendar should not be busy after the event.", calendar.isBusyAt(afterTime));
  }
}