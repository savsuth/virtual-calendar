package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import model.CalendarModel;
import model.CalendarService;
import model.ICalendarModel;
import model.ICalendarService;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Test Cases for CreateCommandParserTest.
 */

public class CreateCommandParserTest {

  private CreateCommandParser parser;

  /**
   * Sets up the test fixture before each test.
   */
  @Before
  public void setUp() {

    ICalendarModel calendarModel = new CalendarModel();
    ICalendarService calendarService = new CalendarService(calendarModel);
    parser = new CreateCommandParser(calendarService);
  }


  @Test
  public void testMissingEventKeyword() throws Exception {
    String[] tokens = ("create evnt Meeting from "
        + "2025-03-01T09:00 to 2025-03-01T10:00").split("\\s+");
    Command cmd = parser.parse(tokens);
    String result = cmd.execute();
    assertTrue(result.startsWith("Invalid create command"));
  }

  /**
   * Test 2: Single event with autoDecline flag in a normal time-bound event.
   */
  @Test
  public void testAutoDeclineSingleEvent() {
    String[] tokens = ("create event --autoDecline "
        + "Meeting from 2025-03-01T09:00 to 2025-03-01T10:00").split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    try {
      String result = cmd.execute();
      assertEquals("Event created: Meeting", result);
    } catch (Exception e) {
      fail("No exception expected: " + e.getMessage());
    }
  }

  /**
   * Test 3: Single event without autoDecline flag in a normal time-bound event.
   */
  @Test
  public void testNoAutoDeclineSingleEvent() {
    String[] tokens = ("create event Meeting from "
        + "2025-03-01T09:00 to 2025-03-01T10:00").split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    try {
      String result = cmd.execute();
      assertEquals("Event created: Meeting", result);
    } catch (Exception e) {
      fail("No exception expected: " + e.getMessage());
    }
  }


  @Test
  public void testTimeBoundMissingTo() throws Exception {
    String[] tokens = "create event Meeting from 2025-03-01T09:00 2025-03-01T10:00".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    String result = cmd.execute();
    assertEquals("Expected 'to' after start time.", result);
  }

  @Test
  public void testTimeBoundRecurringMissingTimesKeyword() throws Exception {
    String[] tokens = ("create event Meeting "
        + "from 2025-03-01T09:00 to 2025-03-01T10:00 repeats MTWRF for 3").split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    String result = cmd.execute();
    assertEquals("Expected 'times' after occurrence count.", result);
  }

  /**
   * Test 6: Time-bound recurring event with "repeats" and "until" keyword.
   */
  @Test
  public void testTimeBoundRecurringUntil() {
    String[] tokens = ("create event Meeting "
        + "from 2025-03-01T09:00 to 2025-03-01T10:00 repeats MTWRF until 2025-03-15").split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    try {
      String result = cmd.execute();
      assertEquals("Recurring event created: Meeting", result);
    } catch (Exception e) {
      fail("No exception expected: " + e.getMessage());
    }
  }

  /**
   * Test 7: All-day event using the "on" branch with only a date.
   */
  @Test
  public void testAllDayEventWithDate() {
    String[] tokens = "create event Meeting on 2025-03-02".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    try {
      String result = cmd.execute();
      assertEquals("Event created: Meeting", result);
    } catch (Exception e) {
      fail("No exception expected: " + e.getMessage());
    }
  }

  /**
   * Test 8: All-day event using the "on" branch with a date-time.
   */
  @Test
  public void testAllDayEventWithDateTime() {
    String[] tokens = "create event Meeting on 2025-03-02T00:00".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    try {
      String result = cmd.execute();
      assertEquals("Event created: Meeting", result);
    } catch (Exception e) {
      fail("No exception expected: " + e.getMessage());
    }
  }


  @Test
  public void testAllDayRecurringMissingTimesKeyword() throws Exception {
    String[] tokens = "create event Meeting on 2025-03-02 repeats MTWRF for 3".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    String result = cmd.execute();
    assertEquals("Expected 'times' after occurrence count.", result);
  }

  /**
   * Test 10: All-day recurring event with "repeats" and "until" keyword.
   */
  @Test
  public void testAllDayRecurringUntil() {
    String[] tokens = ("create event Meeting on "
        + "2025-03-02 repeats MTWRF until 2025-03-10").split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    try {
      String result = cmd.execute();
      assertEquals("Recurring event created: Meeting", result);
    } catch (Exception e) {
      fail("No exception expected: " + e.getMessage());
    }
  }


  @Test
  public void testInvalidCreateCommandFormat() throws Exception {
    String[] tokens = "create event Meeting in 2025-03-01T09:00".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    String result = cmd.execute();
    assertEquals("Invalid create command format.", result);
  }


  @Test
  public void testCatchBlockInvalidDate() throws Exception {
    String[] tokens = "create event Meeting from invalidDate to 2025-03-01T10:00".split("\\s+");
    Command cmd = parser.parse(tokens);
    String result = cmd.execute();
    assertTrue(result.startsWith("Error processing create command:"));
  }

  @Test
  public void testAllDayEventRepeatsInvalidDayString() throws Exception {
    String[] tokens = "create event Meeting on 2025-03-02 repeats X for 2 times".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);

    String result = cmd.execute();
    assertEquals("Error processing create command: Invalid weekday character: X", result);

  }
}