package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import model.CalendarModel;
import model.CalendarService;
import model.ICalendarService;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Test Cases for CreateRecurringEventCommandTest class.
 */

public class CreateRecurringEventCommandTest {

  private CalendarModel calendar;
  private CreateCommandParser parser;

  /**
   * Sets up the test fixture before each test.
   */
  @Before
  public void setUp() {
    calendar = new CalendarModel();
    ICalendarService calendarService = new CalendarService(calendar);
    parser = new CreateCommandParser(calendarService);
  }

  /**
   * Tests that a valid recurring event command is added to the calendar. The test verifies that the
   * returned message is correct and that the event is present in the calendar.
   */
  @Test
  public void testRecurringEventIsAddedToCalendar() {
    String commandLine = "create event --autoDecline Standup from "
        + "2025-03-01T09:00 to 2025-03-01T09:15 repeats MTWRF until 2025-03-15";
    Command cmd = parser.parse(commandLine.split("\\s+"));
    assertNotNull(cmd);

    try {
      String result = cmd.execute();

      assertEquals("Recurring event created: Standup", result);

      assertFalse(calendar.getAllEvents().isEmpty());

      boolean found = calendar.getAllEvents().stream()
          .anyMatch(e -> e.getSubject().equals("Standup"));
      assertTrue(found);
    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

}