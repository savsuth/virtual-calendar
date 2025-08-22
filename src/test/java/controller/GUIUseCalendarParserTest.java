package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import model.CalendarManager;
import model.ICalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for GUIUseCalendarParser. Verifies whether the Calendar is being used or not.
 */
public class GUIUseCalendarParserTest {

  private IMultiCalendarService service;
  private GUIUseCalendarParser parser;

  @Before
  public void setUp() throws Exception {

    ICalendarManager manager = new CalendarManager();
    service = new MultiCalendarService(manager);

    service.createCalendar("Default", "UTC");
    service.useCalendar("Default");

    parser = new GUIUseCalendarParser(service);
  }

  @Test
  public void testUseExistingCalendar() throws Exception {

    String calendarName = "TestCalendar";
    service.createCalendar(calendarName, "UTC");

    Command cmd = parser.parse(calendarName);
    String result = "";
    try {
      result = cmd.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertEquals("Using calendar: " + calendarName, result);
  }

  @Test
  public void testUseNonExistingCalendar() throws Exception {

    String calendarName = "NonExistingCalendar";
    Command cmd = parser.parse(calendarName);
    String result = "";
    try {
      result = cmd.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertEquals("Calendar not found: " + calendarName, result);
  }
}