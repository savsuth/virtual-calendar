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
 * JUNIT Test Class for {@link GUIEditCalendarParser} which parses Edit Inputs for the Calendar.
 */
public class GUIEditCalendarParserTest {

  private IMultiCalendarService service;
  private GUIEditCalendarParser parser;

  @Before
  public void setUp() throws Exception {

    ICalendarManager manager = new CalendarManager();
    service = new MultiCalendarService(manager);

    service.createCalendar("TestCal", "UTC");
    service.useCalendar("TestCal");

    parser = new GUIEditCalendarParser(service);


  }

  @Test
  public void testValidNameEdit() {

    Command cmd = parser.parse("TestCal", "name", "NewTestCal");
    String result = "";
    try {
      result = cmd.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }

    assertEquals("Calendar name updated to: NewTestCal", result);
  }

  @Test
  public void testValidTimezoneEdit() {

    Command cmd = parser.parse("TestCal", "timezone", "Europe/London");
    String result = "";
    try {
      result = cmd.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }

    assertEquals("Calendar timezone updated to: Europe/London", result);
  }

  @Test
  public void testUnknownPropertyEdit() {

    Command cmd = parser.parse("TestCal", "unknownProp", "SomeValue");
    String result = "";
    try {
      result = cmd.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertEquals("Unknown calendar property: unknownProp", result);
  }

  @Test
  public void testEmptyCalendarName() {

    Command cmd = parser.parse("", "name", "NewName");
    String result = "";
    try {
      result = cmd.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertEquals("Error: Calendar name cannot be empty.", result);
  }

  @Test
  public void testEmptyProperty() {

    Command cmd = parser.parse("TestCal", "", "NewValue");
    String result = "";
    try {
      result = cmd.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertEquals("Error: Calendar property cannot be empty.", result);
  }

  @Test
  public void testEmptyNewValue() {

    Command cmd = parser.parse("TestCal", "name", "");
    String result = "";
    try {
      result = cmd.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertEquals("Error: New value cannot be empty.", result);
  }

  @Test
  public void testEditWithDuplicateName() throws Exception {

    service.createCalendar("ExistingCal", "UTC");

    Command cmd = parser.parse("TestCal", "name", "ExistingCal");
    String result = "";
    try {
      result = cmd.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertEquals("Calendar name already exists or edit failed.", result);
  }

}