package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Test Case for {@link MultiCalendarService} which is the factory of the Model.
 */
public class MultiCalendarServiceTest {

  private ICalendarManager manager;
  private IMultiCalendarService multiService;

  @Before
  public void setUp() throws Exception {
    manager = new CalendarManager();
    multiService = new MultiCalendarService(manager);
  }

  @Test
  public void testCreateCalendarSuccess() throws Exception {
    boolean created = multiService.createCalendar("Work",
        "America/New_York");
    assertTrue("Calendar should be created successfully", created);
  }

  @Test
  public void testCreateCalendarDuplicate() throws Exception {
    multiService.createCalendar("Personal", "America/Los_Angeles");
    boolean duplicateCreated = multiService.createCalendar("Personal",
        "America/Los_Angeles");
    assertFalse(duplicateCreated);
  }

  @Test
  public void testUseCalendar() throws Exception {
    multiService.createCalendar("Team", "Europe/London");
    boolean used = multiService.useCalendar("Team");
    assertTrue("Using an existing calendar should succeed", used);
  }

  @Test
  public void testDuplicateCalendar() throws Exception {
    multiService.createCalendar("OldName", "America/New_York");
    boolean edited = multiService.editCalendar("OldName", "name",
        "NewName");
    assertTrue(edited);
    multiService.createCalendar("Duplicate", "America/New_York");
    boolean duplicateEdit = multiService.editCalendar("NewName", "name",
        "Duplicate");
    assertFalse(duplicateEdit);
  }

  @Test
  public void testCreateCalendarActuallyAddsToManager() throws Exception {
    boolean result = multiService.createCalendar("TestCal", "Europe/Paris");
    assertTrue(result);

    assertNotNull(manager.getCalendar("TestCal"));
  }

  @Test
  public void testEditCalendarNameRemovesOldCalendar() throws Exception {
    multiService.createCalendar("Original", "UTC");
    multiService.editCalendar("Original", "name", "Renamed");

    assertNull(manager.getCalendar("Original"));
    assertNotNull(manager.getCalendar("Renamed"));
  }

  @Test
  public void testEditCalendarTimezoneMigratesEvents() throws Exception {

    multiService.createCalendar("TimezoneTest", "America/New_York");
    multiService.useCalendar("TimezoneTest");
    multiService.addSingleEvent("Meeting",
        LocalDateTime.of(2023, 1, 1, 9, 0),
        LocalDateTime.of(2023, 1, 1, 10, 0),
        "", "", true, true);

    boolean result = multiService.editCalendar("TimezoneTest", "timezone", "Europe/London");
    assertTrue(result);

    List<Event> events = manager.getCalendar("TimezoneTest").getCalendarService().getAllEvents();
    assertEquals(LocalDateTime.of(2023, 1, 1, 14, 0), events.get(0).getStartDateTime());
  }


  @Test
  public void testAddSingleEventActuallyCreatesEvent() throws Exception {
    multiService.createCalendar("EventTest", "UTC");
    multiService.useCalendar("EventTest");
    multiService.addSingleEvent("TestEvent",
        LocalDateTime.now(),
        LocalDateTime.now().plusHours(1),
        "", "", true, true);

    assertEquals(1, multiService.getAllEvents().size());
  }

  @Test
  public void testGetEventsOnEmptyCalendar() {
    assertNull(multiService.getEventsOn(LocalDate.now()));
  }

  @Test
  public void testGetAllEventsEmptyCalendar() {
    assertNull(multiService.getAllEvents());
  }

  @Test
  public void testIsBusyAtWithConflictingEvent() throws Exception {
    multiService.createCalendar("BusyTest", "UTC");
    multiService.useCalendar("BusyTest");
    LocalDateTime testTime = LocalDateTime.of(2023, 1, 1, 9, 30);
    multiService.addSingleEvent("Conflict",
        LocalDateTime.of(2023, 1, 1, 9, 0),
        LocalDateTime.of(2023, 1, 1, 10, 0),
        "", "", true, true);

    assertTrue(multiService.isBusyAt(testTime));
  }

  @Test
  public void testNewCalendarCreation() throws Exception {

    ICalendarManager calendarManager = new CalendarManager();
    MultiCalendarService service = new MultiCalendarService(calendarManager);

    String expectedName = "Test Calendar";
    String expectedTimezone = "America/New_York";

    boolean created = service.createCalendar(expectedName, expectedTimezone);
    assertTrue("The calendar should be created successfully", created);

    ICalendarContext calendar = calendarManager.getCalendar(expectedName);
    assertNotNull(calendar);
    assertEquals(expectedName, calendar.getName());
    assertEquals(ZoneId.of(expectedTimezone), calendar.getTimezone());
  }

  @Test
  public void testNewCalendarCreation2() throws Exception {
    ICalendarManager calendarManager = new CalendarManager();
    MultiCalendarService service = new MultiCalendarService(calendarManager);

    String expectedName = "Test Calendar";
    String expectedTimezone = "America/New_York";

    service.createCalendar(expectedName, expectedTimezone);
    service.useCalendar(expectedName);
    assertTrue(service.getCurrentCalendarNameAndZone()[0].equals(expectedName));
    assertTrue(service.getCurrentCalendarNameAndZone()[1].equals(expectedTimezone));
  }

  @Test(expected = Exception.class)
  public void invalidTimezone() throws Exception {
    ICalendarManager calendarManager = new CalendarManager();
    MultiCalendarService service = new MultiCalendarService(calendarManager);

    service.createCalendar("Test", "EIGN");
  }

  @Test
  public void testEditCalendarName() throws Exception {
    ICalendarManager calendarManager = new CalendarManager();
    MultiCalendarService service = new MultiCalendarService(calendarManager);

    service.createCalendar("Test", "America/New_York");
    service.useCalendar("Test");

    service.editCalendar("Test", "name", "Renamed");
    service.useCalendar("Renamed");

    service.editCalendar("Renamed", "timezone", "Europe/London");

    assertTrue(service.getCurrentCalendarNameAndZone()[0].equals("Renamed"));
    assertTrue(service.getCurrentCalendarNameAndZone()[1].equals("Europe/London"));


  }

  @Test
  public void testPrintEventsAfterTimezoneChange() throws Exception {
    ICalendarManager calendarManager = new CalendarManager();
    MultiCalendarService service = new MultiCalendarService(calendarManager);
    String calendarName = "TestCalendar";
    String initialTimezone = "UTC";

    service.createCalendar(calendarName, initialTimezone);
    service.useCalendar(calendarName);

    LocalDateTime start = LocalDateTime.of(2025, 4, 10, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 4, 10, 11, 0);
    service.addSingleEvent("Meeting", start, end, "Desc", "Loc", false, false);

    service.editCalendar(calendarName, "timezone", "America/New_York");

    String printedEvents = service.printEventsOn(LocalDate.of(2025, 4, 10));
    assertTrue(printedEvents.contains("06:00"));
  }

  @Test(expected = Exception.class)
  public void addConflictingSingleEvent() throws Exception {
    ICalendarManager calendarManager = new CalendarManager();
    MultiCalendarService service = new MultiCalendarService(calendarManager);

    service.createCalendar("ABC", "America/New_York");
    service.useCalendar("ABC");

    LocalDateTime start1 = LocalDateTime.of(2025, 4, 10, 10, 0);
    LocalDateTime end1 = LocalDateTime.of(2025, 4, 10, 11, 0);
    service.addSingleEvent("Meeting1", start1, end1, "First meeting", "Office", false, false);

    LocalDateTime start2 = LocalDateTime.of(2025, 4, 10, 10, 30);
    LocalDateTime end2 = LocalDateTime.of(2025, 4, 10, 11, 30);

    service.addSingleEvent("Meeting2", start2, end2, "Conflicting meeting", "Office", false,
        false);

  }

  @Test(expected = Exception.class)
  public void addRecurringEventWithConflict() throws Exception {
    ICalendarManager calendarManager = new CalendarManager();
    MultiCalendarService service = new MultiCalendarService(calendarManager);

    service.createCalendar("ABC", "America/New_York");
    service.useCalendar("ABC");

    LocalDateTime start = LocalDateTime.of(2025, 4, 10, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 4, 10, 11, 0);
    service.addSingleEvent("SingleEvent", start, end, "Desc", "Loc", false, false);

    Set<DayOfWeek> recurrenceDays = new HashSet<>();
    recurrenceDays.add(DayOfWeek.THURSDAY);
    LocalDate recurringEndDate = LocalDate.of(2025, 5, 8);

    service.addRecurringEvent("RecurringEvent", start, end, "Recurring Desc", "Recurring Loc",
        false, recurrenceDays, 5, recurringEndDate, false);
  }

  @Test(expected = Exception.class)
  public void addRecurringEventWithConflict2() throws Exception {
    ICalendarManager calendarManager = new CalendarManager();
    MultiCalendarService service = new MultiCalendarService(calendarManager);

    service.createCalendar("ABC", "America/New_York");
    service.useCalendar("ABC");

    LocalDateTime start = LocalDateTime.of(2025, 4, 10, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 4, 10, 11, 0);
    Set<DayOfWeek> recurrenceDays = new HashSet<>();
    recurrenceDays.add(DayOfWeek.THURSDAY);
    LocalDate recurringEndDate = LocalDate.of(2025, 5, 8);
    service.addRecurringEvent("RecurringEvent", start, end, "Recurring Desc", "Recurring Loc",
        false, recurrenceDays, 5, recurringEndDate, false);

    service.addRecurringEvent("Prada", start, end, "Recurring Desc", "Recurring Loc", false,
        recurrenceDays, 5, recurringEndDate, false);
  }

  @Test(expected = Exception.class)
  public void addRecurringEventWithConflict3() throws Exception {
    ICalendarManager calendarManager = new CalendarManager();
    MultiCalendarService service = new MultiCalendarService(calendarManager);

    service.createCalendar("ABC", "America/New_York");
    service.useCalendar("ABC");

    LocalDateTime start = LocalDateTime.of(2025, 4, 10, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 4, 10, 11, 0);
    Set<DayOfWeek> recurrenceDays = new HashSet<>();
    recurrenceDays.add(DayOfWeek.THURSDAY);
    LocalDate recurringEndDate = LocalDate.of(2025, 5, 8);
    service.addRecurringEvent("RecurringEvent", start, end, "Recurring Desc", "Recurring Loc",
        false, recurrenceDays, 5, recurringEndDate, false);

    service.addRecurringEvent("Prada", LocalDateTime.parse("2025-04-17"), end, "Recurring Desc",
        "Recurring Loc", false, recurrenceDays, 5, recurringEndDate, false);
  }

  @Test
  public void testCopySingleEventWithSameTimezone() throws Exception {
    ICalendarManager calendarManager = new CalendarManager();
    MultiCalendarService service = new MultiCalendarService(calendarManager);

    service.createCalendar("Source", "America/New_York");
    service.useCalendar("Source");
    LocalDateTime sourceStart = LocalDateTime.of(2025, 4, 10, 10, 0);
    LocalDateTime sourceEnd = LocalDateTime.of(2025, 4, 10, 11, 0);
    service.addSingleEvent("Meeting", sourceStart, sourceEnd, "Desc", "Loc", false, false);

    service.createCalendar("Target", "America/New_York");
    LocalDateTime targetStart = LocalDateTime.of(2025, 4, 10, 12, 0);
    service.copyEvent("Meeting", sourceStart, "Target", targetStart);

    service.useCalendar("Target");
    List<Event> events = service.getEventsOn(targetStart.toLocalDate());

    boolean found = false;
    for (Event event : events) {
      if ("Meeting".equals(event.getSubject()) && targetStart.equals(event.getStartDateTime())) {
        found = true;
        break;
      }
    }
    assertTrue(found);
  }

  @Test
  public void testCopyEventsBetweenWithSameTimezone() throws Exception {
    ICalendarManager calendarManager = new CalendarManager();
    MultiCalendarService service = new MultiCalendarService(calendarManager);

    service.createCalendar("Source", "America/New_York");
    service.createCalendar("Target", "America/New_York");

    service.useCalendar("Source");
    LocalDateTime event1Start = LocalDateTime.of(2025, 4, 10, 10, 0);
    LocalDateTime event1End = LocalDateTime.of(2025, 4, 10, 11, 0);
    service.addSingleEvent("Event1", event1Start, event1End, "Desc1", "Loc1", false, false);

    LocalDateTime event2Start = LocalDateTime.of(2025, 4, 11, 12, 0);
    LocalDateTime event2End = LocalDateTime.of(2025, 4, 11, 13, 0);
    service.addSingleEvent("Event2", event2Start, event2End, "Desc2", "Loc2", false, false);

    LocalDate sourceStartDate = LocalDate.of(2025, 4, 10);
    LocalDate sourceEndDate = LocalDate.of(2025, 4, 11);
    LocalDate targetStartDate = LocalDate.of(2025, 5, 1);

    service.copyEventsBetween(sourceStartDate, sourceEndDate, "Target", targetStartDate);

    service.useCalendar("Target");
    List<Event> events = service.getAllEvents();
    assertEquals(2, events.size());

    boolean foundEvent1 = false;
    boolean foundEvent2 = false;
    for (Event event : events) {
      if ("Event1".equals(event.getSubject())) {
        foundEvent1 = true;
      }
      if ("Event2".equals(event.getSubject())) {
        foundEvent2 = true;
      }
    }
    assertTrue(foundEvent1);
    assertTrue(foundEvent2);
  }

  @Test
  public void testCopyEventsBetweenWithDifferentTimezones() throws Exception {
    ICalendarManager calendarManager = new CalendarManager();
    MultiCalendarService service = new MultiCalendarService(calendarManager);

    service.createCalendar("Source", "America/New_York");
    service.createCalendar("Target", "Asia/Kolkata");

    service.useCalendar("Source");
    LocalDateTime event1Start = LocalDateTime.of(2025, 4, 10, 10, 0);
    LocalDateTime event1End = LocalDateTime.of(2025, 4, 10, 11, 0);
    service.addSingleEvent("Event1", event1Start, event1End, "Desc1", "Loc1", false, false);

    LocalDateTime event2Start = LocalDateTime.of(2025, 4, 11, 12, 0);
    LocalDateTime event2End = LocalDateTime.of(2025, 4, 11, 13, 0);
    service.addSingleEvent("Event2", event2Start, event2End, "Desc2", "Loc2", false, false);

    LocalDate sourceStartDate = LocalDate.of(2025, 4, 10);
    LocalDate sourceEndDate = LocalDate.of(2025, 4, 11);
    LocalDate targetStartDate = LocalDate.of(2025, 5, 1);

    service.copyEventsBetween(sourceStartDate, sourceEndDate, "Target", targetStartDate);

    service.useCalendar("Target");
    List<Event> events = service.getAllEvents();
    assertEquals(2, events.size());

    boolean foundEvent1 = false;
    boolean foundEvent2 = false;
    for (Event event : events) {
      if ("Event1".equals(event.getSubject())) {
        foundEvent1 = true;
      }
      if ("Event2".equals(event.getSubject())) {
        foundEvent2 = true;
      }
    }
    assertTrue(foundEvent1);
    assertTrue(foundEvent2);

  }


  @Test
  public void testIsBusyAtWhenEmpty() {
    assertFalse(multiService.isBusyAt(LocalDateTime.now()));
  }

  @Test
  public void testUseCalendarNonexistent() throws Exception {
    assertFalse(multiService.useCalendar("NonExistentCalendar"));
  }

  @Test
  public void testEditCalendarInvalidProperty() throws Exception {
    multiService.createCalendar("Cal1", "UTC");
    assertFalse(multiService.editCalendar("Cal1", "invalidProp", "someValue"));
  }

  @Test(expected = Exception.class)
  public void testCopyEventNonExistentTargetCalendar() throws Exception {
    multiService.createCalendar("SourceCal", "UTC");
    multiService.useCalendar("SourceCal");

    LocalDateTime sourceStart = LocalDateTime.of(2025, 4, 10, 10, 0);
    multiService.addSingleEvent("Event1", sourceStart, sourceStart.plusHours(1),
        "Description", "Location", true, false);
    multiService.copyEvent("Event1", sourceStart, "TargetCal",
        LocalDateTime.of(2025, 4, 10, 12, 0));
  }

  @Test
  public void testGetEventsOnAndGetAllEventsWhenNoActiveCalendar() {

    ICalendarManager emptyManager = new CalendarManager();
    IMultiCalendarService serviceWithoutActive = new MultiCalendarService(emptyManager);
    assertNull(serviceWithoutActive.getEventsOn(LocalDate.now()));
    assertNull(serviceWithoutActive.getAllEvents());
  }

  @Test
  public void testIsBusyAtWhenNoActiveCalendar() {
    ICalendarManager emptyManager = new CalendarManager();
    IMultiCalendarService serviceWithoutActive = new MultiCalendarService(emptyManager);
    assertFalse(serviceWithoutActive.isBusyAt(LocalDateTime.now()));
  }

  @Test(expected = Exception.class)
  public void testCopyEventsOnNonExistentTargetCalendar() throws Exception {
    multiService.createCalendar("Source", "UTC");
    multiService.useCalendar("Source");
    multiService.copyEventsOn(LocalDate.now(), "NonExistentTarget", LocalDate.now());
  }

}
