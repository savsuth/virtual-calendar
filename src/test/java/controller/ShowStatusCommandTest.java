package controller;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import model.CalendarModel;
import model.CalendarService;
import model.ICalendarService;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the ShowStatusCommand class.
 */
public class ShowStatusCommandTest {

  private ICalendarService calendar;

  @Before
  public void setUp() throws Exception {
    CalendarModel calendara = new CalendarModel();
    calendar = new CalendarService(calendara);
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    calendar.addSingleEvent("Meeting", start, end, "", "", true, false);
  }

  /**
   * Tests that the status is "Busy" when the check time falls within an event.
   */
  @Test
  public void testShowStatusBusy() {
    LocalDateTime checkTime = LocalDateTime.of(2025, 3, 1, 9, 30);
    ShowStatusCommand cmd = new ShowStatusCommand(calendar, checkTime);
    String result = cmd.execute();
    assertEquals("Busy", result);
  }

  /**
   * Tests that the status is "Available" when the check time does not fall within any event.
   */
  @Test
  public void testShowStatusAvailable() {
    LocalDateTime checkTime = LocalDateTime.of(2025, 3, 1, 10, 30);
    ShowStatusCommand cmd = new ShowStatusCommand(calendar, checkTime);
    String result = cmd.execute();
    assertEquals("Available", result);
  }
}
