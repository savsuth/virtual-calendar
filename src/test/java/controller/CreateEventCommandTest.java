package controller;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import model.CalendarModel;
import model.CalendarService;
import model.ICalendarService;
import model.InvalidDateException;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the CreateEventCommand class.
 */
public class CreateEventCommandTest {

  private CalendarModel calendar;
  private ICalendarService calendarService;

  /**
   * Sets up the test fixture before each test.
   */
  @Before
  public void setUp() {
    calendar = new CalendarModel();
    calendarService = new CalendarService(calendar);
  }


  @Test
  public void testExecuteCreateEvent() throws Exception {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    CreateEventCommand cmd = new CreateEventCommand(calendarService, false,
        "Meeting", start, end, "desc", "room", true);
    String result = cmd.execute();
    assertEquals("Event created: Meeting", result);
    assertEquals(1, calendar.getAllEvents().size());
  }


  @Test(expected = InvalidDateException.class)
  public void testCreateEventInvalidDate() throws Exception {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 9, 0);
    new CreateEventCommand(calendarService, false, "Meeting", start, end, "", "", true).execute();
  }
}