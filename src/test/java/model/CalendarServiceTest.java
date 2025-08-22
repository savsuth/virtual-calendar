package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.Test;

/**
 * Unit tests for CalendarService. These tests ensure event creation, editing, export, printing, and
 * conflict detection are functioning correctly.
 */

public class CalendarServiceTest {

  @Test
  public void testAddSingleEventAndGetAll() throws InvalidDateException, EventConflictException {
    CalendarModel model = new CalendarModel();
    CalendarService service = new CalendarService(model);
    service.addSingleEvent("Meeting",
        LocalDateTime.of(2025, 3, 27, 10, 0),
        LocalDateTime.of(2025, 3, 27, 11, 0),
        "Team meeting", "Room A", true, true);
    List<Event> events = service.getAllEvents();
    assertEquals(1, events.size());
    assertEquals("Meeting", events.get(0).getSubject());
  }

  @Test
  public void testAddRecurringEventAndOccurrences()
      throws InvalidDateException, EventConflictException {
    CalendarModel model = new CalendarModel();
    CalendarService service = new CalendarService(model);
    java.util.Set<java.time.DayOfWeek> days = new java.util.HashSet<>();
    days.add(java.time.DayOfWeek.MONDAY);
    service.addRecurringEvent("Workshop",
        LocalDateTime.of(2025, 4, 7, 9, 0),
        LocalDateTime.of(2025, 4, 7, 10, 0),
        "Weekly workshop", "Lab", true, days, 3,
        LocalDate.of(2025, 4, 21), true);
    List<Event> events = service.getAllEvents();
    List<Event> occs = events.get(0).getOccurrences();
    assertEquals(3, occs.size());
  }

  @Test
  public void testGetEventsOn() throws Exception {
    CalendarModel model = new CalendarModel();
    CalendarService service = new CalendarService(model);
    service.addSingleEvent("Breakfast",
        LocalDateTime.of(2025, 3, 27, 8, 0),
        LocalDateTime.of(2025, 3, 27, 9, 0),
        "Morning meal", "Cafe", true, true);
    List<Event> events = service.getEventsOn(LocalDate.of(2025, 3, 27));
    assertEquals(1, events.size());
  }

  @Test
  public void testIsBusyAt() throws Exception {
    CalendarModel model = new CalendarModel();
    CalendarService service = new CalendarService(model);
    service.addSingleEvent("Lunch",
        LocalDateTime.of(2025, 3, 27, 12, 0),
        LocalDateTime.of(2025, 3, 27, 13, 0),
        "Team lunch", "Cafeteria", true, true);
    assertTrue(service.isBusyAt(LocalDateTime.of(2025, 3, 27,
        12, 30)));
    assertFalse(service.isBusyAt(LocalDateTime.of(2025, 3, 27,
        14, 0)));
  }

  @Test
  public void testEditEvent() throws Exception {
    CalendarModel model = new CalendarModel();
    CalendarService service = new CalendarService(model);
    service.addSingleEvent("Seminar",
        LocalDateTime.of(2025, 3, 27, 15, 0),
        LocalDateTime.of(2025, 3, 27, 16, 0),
        "Tech Seminar", "Auditorium", true, true);
    service.editEvent("Seminar", LocalDateTime.of(2025, 3, 27,
            15, 0), "subject", "Updated Seminar",
        ICalendarService.EditMode.SINGLE);
    List<Event> events = service.getAllEvents();
    assertEquals("Updated Seminar", events.get(0).getSubject());
  }

  @Test
  public void testExporttoCSV() throws IOException, InvalidDateException, EventConflictException {
    CalendarModel model = new CalendarModel();
    CalendarService service = new CalendarService(model);
    service.addSingleEvent("Dinner",
        LocalDateTime.of(2025, 3, 27, 19, 0),
        LocalDateTime.of(2025, 3, 27, 20, 0),
        "Family dinner", "Home", true, true);
    String filePath = service.exportTo("csv", "test_export.csv");
    assertNotNull(filePath);
    File file = new File(filePath);
    assertTrue(file.exists());
    file.delete();
  }

  @Test
  public void testPrintEventsOn() throws Exception {
    CalendarModel model = new CalendarModel();
    CalendarService service = new CalendarService(model);
    service.addSingleEvent("Meeting",
        LocalDateTime.of(2025, 3, 27, 10, 0),
        LocalDateTime.of(2025, 3, 27, 11, 0),
        "Discussion", "Room X", true, true);
    String output = service.printEventsOn(LocalDate.of(2025, 3, 27));
    assertTrue(output.contains("Meeting"));
  }

  @Test
  public void testPrintEventsRange() throws Exception {
    CalendarModel model = new CalendarModel();
    CalendarService service = new CalendarService(model);
    service.addSingleEvent("Conference",
        LocalDateTime.of(2025, 3, 28, 9, 0),
        LocalDateTime.of(2025, 3, 28, 17, 0),
        "Annual conference", "Convention Center",
        true, true);
    String output = service.printEventsRange(LocalDateTime.of(2025, 3,
        28, 8, 0), LocalDateTime.of(2025, 3,
        28, 18, 0));
    assertTrue(output.contains("Conference"));
  }

  @Test(expected = EventConflictException.class)
  public void testAddEventConflict() throws Exception {
    CalendarModel model = new CalendarModel();
    CalendarService service = new CalendarService(model);
    service.addSingleEvent("ConflictTest",
        LocalDateTime.of(2025, 3, 27, 9, 0),
        LocalDateTime.of(2025, 3, 27, 10, 0),
        "Desc", "Room", true, true);
    service.addSingleEvent("ConflictTest",
        LocalDateTime.of(2025, 3, 27, 9, 30),
        LocalDateTime.of(2025, 3, 27, 10, 30),
        "Desc", "Room", true, true);
  }

  @Test
  public void testBusyAtEdge() throws Exception {
    CalendarModel model = new CalendarModel();
    CalendarService service = new CalendarService(model);
    service.addSingleEvent("EdgeTest", LocalDateTime.of(2025, 3,
            27, 14, 0),
        LocalDateTime.of(2025, 3, 27, 15, 0),
        "Desc", "Room", true, true);
    assertFalse(service.isBusyAt(LocalDateTime.of(2025, 3, 27,
        15, 0)));
    assertTrue(service.isBusyAt(LocalDateTime.of(2025, 3, 27,
        14, 59)));
  }

  @Test(expected = InvalidDateException.class)
  public void testAddEventInvalidDates() throws Exception {
    CalendarModel model = new CalendarModel();
    CalendarService service = new CalendarService(model);
    service.addSingleEvent("Event",
        LocalDateTime.of(2025, 3, 27, 12, 0),
        LocalDateTime.of(2025, 3, 27, 11, 0),
        "desc", "room", true, true);
  }
}

