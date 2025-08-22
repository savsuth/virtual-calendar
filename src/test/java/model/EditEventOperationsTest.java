package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

/**
 * Unit tests for the EditEventOperations class. These tests cover editing both single and recurring
 * events using different modes like SINGLE, ALL, and FROM.
 */
public class EditEventOperationsTest {

  @Test
  public void testEditSingleEventSuccess() throws Exception {
    CalendarModel model = new CalendarModel();
    CalendarService service = new CalendarService(model);
    service.addSingleEvent("Meeting",
        LocalDateTime.of(2025, 5, 1, 10, 0),
        LocalDateTime.of(2025, 5, 1, 11, 0),
        "Desc", "Room", true, true);
    new EditEventOperations().editEvent(model, "Meeting",
        LocalDateTime.of(2025, 5, 1, 10, 0),
        "subject", "UpdatedMeeting", ICalendarService.EditMode.SINGLE);
    List<Event> events = model.getAllEvents();
    assertEquals("UpdatedMeeting", events.get(0).getSubject());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testEditSingleEventUnsupportedMode() throws Exception {
    CalendarModel model = new CalendarModel();
    CalendarService service = new CalendarService(model);
    service.addSingleEvent("Meeting",
        LocalDateTime.of(2025, 5, 1, 10, 0),
        LocalDateTime.of(2025, 5, 1, 11, 0),
        "Desc", "Room", true, true);
    new EditEventOperations().editEvent(model, "Meeting", LocalDateTime.of(2025,
            5, 1, 10, 0), "subject", "FailUpdate",
        ICalendarService.EditMode.ALL);
  }

  @Test
  public void testEditRecurringEventAllMode() throws Exception {
    CalendarModel model = new CalendarModel();
    RecurringEvent re = new RecurringEvent("Recurring",
        LocalDateTime.of(2025, 5, 2, 9, 0),
        LocalDateTime.of(2025, 5, 2, 10, 0),
        "Desc", "Room", true,
        Collections.singleton(DayOfWeek.FRIDAY),
        3, LocalDate.of(2025, 5, 16));
    re.setAutoDecline(true);
    model.addEvent(re, true);
    new EditEventOperations().editEvent(model, "Recurring", LocalDateTime.of(2025,
            5, 2, 9, 0), "location", "UpdatedRoom",
        ICalendarService.EditMode.ALL);
    List<Event> events = model.getAllEvents();
    RecurringEvent updated = (RecurringEvent) events.get(0);
    assertEquals("UpdatedRoom", updated.getLocation());
  }

  @Test(expected = Exception.class)
  public void testEditRecurringEventSingleModeNotFound() throws Exception {
    CalendarModel model = new CalendarModel();
    RecurringEvent reccuring = new RecurringEvent("Class",
        LocalDateTime.of(2025, 5, 8, 9, 0),
        LocalDateTime.of(2025, 5, 8, 10, 0),
        "Lecture", "Hall", true,
        Collections.singleton(DayOfWeek.THURSDAY),
        2, LocalDate.of(2025, 5, 15));
    reccuring.setAutoDecline(true);
    model.addEvent(reccuring, true);
    new EditEventOperations().editEvent(model, "Class", LocalDateTime.of(2025,
            5, 9, 9, 0), "subject", "NewClass",
        ICalendarService.EditMode.SINGLE);
  }

  @Test
  public void testEditRecurringEventFromMode() throws Exception {
    CalendarModel model = new CalendarModel();
    RecurringEvent re = new RecurringEvent("Seminar",
        LocalDateTime.of(2025, 5, 1, 14, 0),
        LocalDateTime.of(2025, 5, 1, 15, 0),
        "Talk", "Auditorium", true,
        Collections.singleton(DayOfWeek.FRIDAY),
        4, LocalDate.of(2025, 5, 29));
    re.setAutoDecline(true);
    model.addEvent(re, true);
    int beforeCount = model.getAllEvents().size();
    new EditEventOperations().editEvent(model, "Seminar", LocalDateTime.of(2025,
            5, 8, 14, 0), "description",
        "UpdatedTalk", ICalendarService.EditMode.FROM);
    int afterCount = model.getAllEvents().size();
    assertTrue(afterCount > beforeCount);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testEditEventInvalidProperty() throws Exception {
    CalendarModel model = new CalendarModel();
    CalendarService service = new CalendarService(model);
    service.addSingleEvent("Event",
        LocalDateTime.of(2025, 5, 1, 10, 0),
        LocalDateTime.of(2025, 5, 1, 11, 0),
        "Desc", "Room", true, true);
    new EditEventOperations().editEvent(model, "Event", LocalDateTime.of(2025,
            5, 1, 10, 0), "invalid", "value",
        ICalendarService.EditMode.SINGLE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateEventStartTimeConflict() throws Exception {
    CalendarModel model = new CalendarModel();
    CalendarService service = new CalendarService(model);
    service.addSingleEvent("Conflict",
        LocalDateTime.of(2025, 5, 1, 10, 0),
        LocalDateTime.of(2025, 5, 1, 11, 0),
        "Desc", "Room", true, true);
    new EditEventOperations().editEvent(model, "Conflict", LocalDateTime.of(2025,
            5, 1, 10, 0), "start",
        "2025-05-01T11:30", ICalendarService.EditMode.SINGLE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateEventEndTimeConflict() throws Exception {
    CalendarModel model = new CalendarModel();
    CalendarService service = new CalendarService(model);
    service.addSingleEvent("TestEnd",
        LocalDateTime.of(2025, 5, 1, 10, 0),
        LocalDateTime.of(2025, 5, 1, 11, 0),
        "Desc", "Room", true, true);
    new EditEventOperations().editEvent(model, "TestEnd", LocalDateTime.of(2025,
            5, 1, 10, 0), "end",
        "2025-05-01T09:30", ICalendarService.EditMode.SINGLE);
  }

  @Test
  public void testEditEventNoMatch() {
    try {
      CalendarModel model = new CalendarModel();
      CalendarService service = new CalendarService(model);
      service.addSingleEvent("NoMatch",
          LocalDateTime.of(2025, 5, 1, 10, 0),
          LocalDateTime.of(2025, 5, 1, 11, 0),
          "Desc", "Room", true, true);
      new EditEventOperations().editEvent(model, "NoMatch",
          LocalDateTime.of(2025, 5, 1, 12, 0),
          "subject", "New", ICalendarService.EditMode.SINGLE);
      fail("Expected exception for no matching event");
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("No matching event"));
    }


  }

  @Test
  public void testEditSingleEventSetAutoDecline() throws Exception {
    CalendarModel model = new CalendarModel();
    CalendarService service = new CalendarService(model);

    service.addSingleEvent("TeamEvent",
        LocalDateTime.of(2025, 10, 1, 9, 0),
        LocalDateTime.of(2025, 10, 1, 10, 0),
        "Desc", "Room", true, false);

    new EditEventOperations().editEvent(model, "TeamEvent",
        LocalDateTime.of(2025, 10, 1, 9, 0),
        "autodecline", "anyValue",
        ICalendarService.EditMode.SINGLE);

    Event e = model.getAllEvents().get(0);
    assertTrue("Expected autoDecline to be set to true", e.isAutoDecline());
  }

  @Test
  public void testEditSingleEventSetPublicFlag() throws Exception {
    CalendarModel model = new CalendarModel();
    CalendarService service = new CalendarService(model);

    service.addSingleEvent("PublicEvent",
        LocalDateTime.of(2025, 9, 1, 9, 0),
        LocalDateTime.of(2025, 9, 1, 10, 0),
        "Desc", "Office", true, false);

    new EditEventOperations().editEvent(model, "PublicEvent",
        LocalDateTime.of(2025, 9, 1, 9, 0),
        "public", "false",
        ICalendarService.EditMode.SINGLE);

    Event e = model.getAllEvents().get(0);
    assertFalse("Expected event to become private", e.isPublic());
  }


  @Test
  public void testRecurringEventFromModeSplitCreated() throws Exception {
    CalendarModel model = new CalendarModel();
    CalendarService service = new CalendarService(model);

    service.addRecurringEvent("BiWeekly",
        LocalDateTime.of(2025, 6, 1, 14, 0),
        LocalDateTime.of(2025, 6, 1, 15, 0),
        "OldDesc", "Cafe", true,
        java.util.Collections.singleton(java.time.DayOfWeek.SUNDAY),
        4,
        java.time.LocalDate.of(2025, 6, 28),
        false);

    new EditEventOperations().editEvent(model, "BiWeekly",
        LocalDateTime.of(2025, 6, 8, 14, 0),
        "location", "UpdatedCafe",
        ICalendarService.EditMode.FROM);

    List<Event> all = model.getAllEvents();
    assertEquals("Expect 2 events after splitting (the old truncated + the new).", 2, all.size());

    Event second = all.get(1);
    assertEquals("Expected location in new recurring event to be UpdatedCafe", "UpdatedCafe",
        second.getLocation());
  }

  @Test
  public void testEditEventDescription() throws Exception {
    CalendarModel model = new CalendarModel();
    model.addEvent(new SingleEvent("Meeting",
        LocalDateTime.of(2025, 7, 1, 14, 0),
        LocalDateTime.of(2025, 7, 1, 15, 0),
        "OldDesc", "Room", true), true);

    new EditEventOperations().editEvent(model, "Meeting",
        LocalDateTime.of(2025, 7, 1, 14, 0),
        "description", "NewDesc",
        ICalendarService.EditMode.SINGLE);

    assertEquals("NewDesc", model.getAllEvents().get(0).getDescription());
  }

  @Test
  public void testEditStartWithoutAutoDecline() throws Exception {
    CalendarModel model = new CalendarModel();

    model.addEvent(new SingleEvent("Flexible",
        LocalDateTime.of(2025, 8, 1, 10, 0),
        LocalDateTime.of(2025, 8, 1, 11, 0),
        "", "Room", true), false);

    new EditEventOperations().editEvent(model, "Flexible",
        LocalDateTime.of(2025, 8, 1, 10, 0),
        "start", "2025-08-01T09:30",
        ICalendarService.EditMode.SINGLE);

    assertEquals(LocalDateTime.of(2025, 8, 1, 9, 30),
        model.getAllEvents().get(0).getStartDateTime());
  }


}
