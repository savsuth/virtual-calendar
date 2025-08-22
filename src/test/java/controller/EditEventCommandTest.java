package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import model.CalendarModel;
import model.CalendarService;
import model.InvalidDateException;
import model.SingleEvent;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Test Case for {@link EditEventCommand}.
 */

public class EditEventCommandTest {

  private CalendarModel calendar;
  private CalendarService service;

  @Before
  public void setUp() {
    calendar = new CalendarModel();
    service = new CalendarService(calendar);
  }

  @Test
  public void testEditSingleEvent() throws Exception {
    service.addSingleEvent("Meeting",
        LocalDateTime.of(2025, 3, 1, 9, 0),
        LocalDateTime.of(2025, 3, 1, 10, 0),
        "", "", true, false);
    LocalDateTime target = LocalDateTime.of(2025, 3, 1, 9, 0);
    EditEventCommand cmd = new EditEventCommand(service, "Meeting", target, "subject",
        "TeamMeeting", EditEventCommand.EditMode.SINGLE);
    String result = cmd.execute();
    assertTrue(result.contains("Edited event(s)"));
    assertEquals("TeamMeeting", calendar.getAllEvents().get(0).getSubject());
  }

  @Test(expected = Exception.class)
  public void testEditEventNotFound() throws Exception {
    LocalDateTime target = LocalDateTime.of(2025, 3, 1, 11, 0);
    EditEventCommand cmd = new EditEventCommand(service, "Meeting", target, "subject",
        "TeamMeeting", EditEventCommand.EditMode.SINGLE);
    String result = cmd.execute();
  }


  @Test
  public void testEditLocationForSingleEvent() throws Exception {
    service.addSingleEvent("Meeting",
        LocalDateTime.of(2025, 3, 1, 9, 0),
        LocalDateTime.of(2025, 3, 1, 10, 0),
        "", "", true, false);
    LocalDateTime target = LocalDateTime.of(2025, 3, 1, 9, 0);
    EditEventCommand cmd = new EditEventCommand(service, "Meeting", target, "location", "Room202",
        EditEventCommand.EditMode.SINGLE);
    String result = cmd.execute();
    assertTrue(result.contains("Edited event(s)"));
    assertEquals("Room202", calendar.getAllEvents().get(0).getLocation());
  }

  @Test
  public void testEditEndTimeForSingleEvent() throws Exception {
    service.addSingleEvent("Meeting",
        LocalDateTime.of(2025, 3, 1, 9, 0),
        LocalDateTime.of(2025, 3, 1, 10, 0),
        "", "", true, false);
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    EditEventCommand cmd = new EditEventCommand(service, "Meeting", start, "end",
        "2025-03-01T11:00", EditEventCommand.EditMode.SINGLE);
    String result = cmd.execute();
    assertTrue(result.contains("Edited event(s)"));
    SingleEvent updatedEvent = (SingleEvent) calendar.getAllEvents().get(0);
    assertEquals(LocalDateTime.of(2025, 3, 1, 11, 0), updatedEvent.getEffectiveEndDateTime());
  }

  @Test
  public void testEditStartTimeAfterEndTimeShouldFail() {
    try {
      service.addSingleEvent("Meeting",
          LocalDateTime.of(2025, 3, 1, 9, 0),
          LocalDateTime.of(2025, 3, 1, 10, 0),
          "", "", true, false);
    } catch (Exception e) {
      fail(e.getMessage());
    }
    LocalDateTime target = LocalDateTime.of(2025, 3, 1, 9, 0);
    EditEventCommand cmd = new EditEventCommand(service, "Meeting", target, "start",
        "2025-03-01T13:00", EditEventCommand.EditMode.SINGLE, false);
    try {
      cmd.execute();
      fail();
    } catch (IllegalArgumentException | InvalidDateException e) {
      assertEquals("Start time cannot be after end time.", e.getMessage());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }


  @Test
  public void testEditEndTimeBeforeStartTimeShouldFail() {
    try {
      service.addSingleEvent("Meeting",
          LocalDateTime.of(2025, 3, 1, 9, 0),
          LocalDateTime.of(2025, 3, 1, 10, 0),
          "", "", true, false);
    } catch (Exception e) {
      fail(e.getMessage());
    }
    LocalDateTime target = LocalDateTime.of(2025, 3, 1, 9, 0);
    EditEventCommand cmd = new EditEventCommand(service, "Meeting", target, "end",
        "2025-03-01T08:00", EditEventCommand.EditMode.SINGLE, false);
    try {
      cmd.execute();
      fail();
    } catch (IllegalArgumentException | InvalidDateException e) {
      assertEquals("End time cannot be before start time.", e.getMessage());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testEditTimeConflictPrevention() throws Exception {
    LocalDateTime startA = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime endA = LocalDateTime.of(2025, 3, 1, 10, 0);
    service.addSingleEvent("Meeting", startA, endA, "", "", true, false);
    LocalDateTime startB = LocalDateTime.of(2025, 3, 1, 11, 0);
    LocalDateTime endB = LocalDateTime.of(2025, 3, 1, 12, 0);
    service.addSingleEvent("Meeting", startB, endB, "", "", true, false);
    EditEventCommand cmd = new EditEventCommand(service, "Meeting", startB, "start",
        "2025-03-01T09:30", EditEventCommand.EditMode.SINGLE, true);
    try {
      cmd.execute();
    } catch (IllegalArgumentException e) {
      assertEquals("Edit would cause a conflict.", e.getMessage());
    }
  }

  @Test
  public void testEditPublicFlagForSingleEvent() throws Exception {
    service.addSingleEvent("Meeting",
        LocalDateTime.of(2025, 3, 1, 9, 0),
        LocalDateTime.of(2025, 3, 1, 10, 0),
        "", "", true, false);
    LocalDateTime target = LocalDateTime.of(2025, 3, 1, 9, 0);
    EditEventCommand cmd = new EditEventCommand(service, "Meeting", target, "public", "false",
        EditEventCommand.EditMode.SINGLE);
    String result = cmd.execute();
    assertTrue(result.contains("Edited event(s)"));
    boolean isPublic = calendar.getAllEvents().get(0).isPublic();
    assertFalse(isPublic);
  }
}





