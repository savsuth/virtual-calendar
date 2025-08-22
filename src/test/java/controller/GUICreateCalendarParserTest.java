package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import model.CalendarManager;
import model.ICalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Cases for the {@link GUICreateCalendarParser} class.
 */
public class GUICreateCalendarParserTest {

  private GUICreateCalendarParser parser;

  /**
   * Initializes the calendar service and parser before each test.
   */
  @Before
  public void setUp() {
    ICalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);
    parser = new GUICreateCalendarParser(service);
  }

  @Test
  public void parseValidCreation() {

    String calendarName = "TestCalendar";
    String timezone = "UTC";
    Command command = parser.parse(calendarName, timezone);
    String result = "";
    try {
      result = command.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertEquals("Calendar created: " + calendarName, result);
  }

  @Test
  public void parseDuplicateCreation() {

    String calendarName = "TestCalendarDup";
    String timezone = "UTC";
    Command command1 = parser.parse(calendarName, timezone);
    String result1 = "";
    try {
      result1 = command1.execute();
    } catch (Exception e) {
      fail("Execution threw an exception on first creation: " + e.getMessage());
    }
    assertEquals("Calendar created: " + calendarName, result1);

    Command command2 = parser.parse(calendarName, timezone);
    String result2 = "";
    try {
      result2 = command2.execute();
    } catch (Exception e) {
      fail("Execution threw an exception on duplicate creation: " + e.getMessage());
    }
    assertEquals("Calendar creation failed: Duplicate calendar name.", result2);
  }

  @Test
  public void parseInvalidTimezone() {

    String calendarName = "InvalidTimeZoneCalendar";
    String invalidTimezone = "invalid";
    Command command = parser.parse(calendarName, invalidTimezone);
    String result = "";
    try {
      result = command.execute();
    } catch (Exception e) {
      fail("Execution threw an exception with invalid timezone: " + e.getMessage());
    }

    assertTrue(result.startsWith("Calendar creation failed:"));
    assertTrue(result.contains("Invalid timezone"));
  }
}