package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.CalendarManager;
import model.ICalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the GUIEditEventParser class.
 */
public class GUIEditEventParserTest {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd'T'HH:mm");
  private GUIEditEventParser parser;

  /**
   * Prepares a default calendar and adds a single event before each test runs.
   */
  @Before
  public void setUp() throws Exception {

    ICalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);

    service.createCalendar("Default", "UTC");
    service.useCalendar("Default");

    service.addSingleEvent("Meeting",
        LocalDateTime.parse("2025-04-10T10:00", FORMATTER),
        LocalDateTime.parse("2025-04-10T11:00", FORMATTER),
        "Team meeting", "Office", true, false);

    parser = new GUIEditEventParser(service);
  }

  /**
   * Tests successful name update for an existing calendar.
   */
  @Test
  public void testValidEditEventSingleMode() {

    Command cmd = parser.parse("Meeting", "2025-04-10T10:00", "subject", "UpdatedMeeting",
        "SINGLE");
    String result = "";
    try {
      result = cmd.execute();
    } catch (Exception e) {
      fail("Valid edit event threw an exception: " + e.getMessage());
    }

    assertEquals("Edited event(s) 'Meeting': subject changed to UpdatedMeeting", result);
  }

  @Test
  public void testEmptySubject() {
    Command cmd = parser.parse("", "2025-04-10T10:00", "subject", "NewValue", "SINGLE");
    String result = "";
    try {
      result = cmd.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertEquals("Error: Subject cannot be empty.", result);
  }

  @Test
  public void testEmptyProperty() {
    Command cmd = parser.parse("Meeting", "2025-04-10T10:00", "", "NewValue", "SINGLE");
    String result = "";
    try {
      result = cmd.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertEquals("Error: Property cannot be empty.", result);
  }

  @Test
  public void testEmptyNewValue() {
    Command cmd = parser.parse("Meeting", "2025-04-10T10:00", "subject", "", "SINGLE");
    String result = "";
    try {
      result = cmd.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertEquals("Error: New value cannot be empty.", result);
  }

  @Test
  public void testInvalidEditMode() {

    Command cmd = parser.parse("Meeting", "2025-04-10T10:00", "subject", "NewValue",
        "INVALID_MODE");
    String result = "";
    try {
      result = cmd.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertEquals("Error: Invalid EditMode: INVALID_MODE", result);
  }

  @Test
  public void testMissingStartTimeForSingleMode() {

    Command cmd = parser.parse("Meeting", "", "subject", "NewValue", "SINGLE");
    String result = "";
    try {
      result = cmd.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertEquals("Error: Start time is required for SINGLE or FROM mode.", result);
  }

  @Test
  public void testInvalidStartTimeFormat() {

    Command cmd = parser.parse("Meeting", "invalidTime", "subject", "NewValue", "SINGLE");
    String result = "";
    try {
      result = cmd.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }

    assertTrue(result.startsWith("Error parsing start time:"));
  }

}