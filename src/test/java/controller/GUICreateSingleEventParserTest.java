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
 * JUNIT Test Class for {@link GUICreateSingleEventParserTest} which parses SingleEvents.
 */
public class GUICreateSingleEventParserTest {

  private GUICreateSingleEventParser parser;

  @Before
  public void setUp() throws Exception {

    ICalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);

    service.createCalendar("Default", "UTC");
    service.useCalendar("Default");
    parser = new GUICreateSingleEventParser(service);
  }

  @Test
  public void testValidSingleEventCreation() {
    String subject = "Meeting";
    String start = "2025-04-10T10:00";
    String end = "2025-04-10T11:00";
    String description = "Project meeting";
    String location = "Conference Room";
    boolean isPublic = true;
    boolean autoDecline = false;

    Command command = parser.parse(subject, start, end, description, location, isPublic,
        autoDecline);
    String result = "";
    try {
      result = command.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertEquals("Event created: " + subject, result);
  }

  @Test
  public void testEmptySubject() {
    String subject = "";
    String start = "2025-04-10T10:00";
    String end = "2025-04-10T11:00";
    String description = "Some description";
    String location = "Room 1";
    boolean isPublic = true;
    boolean autoDecline = false;

    Command command = parser.parse(subject, start, end, description, location, isPublic,
        autoDecline);
    String result = "";
    try {
      result = command.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertEquals("Error: Subject cannot be empty.", result);
  }

  @Test
  public void testEmptyStart() {
    String subject = "Meeting";
    String start = "";
    String end = "2025-04-10T11:00";
    String description = "Some description";
    String location = "Room 1";
    boolean isPublic = true;
    boolean autoDecline = false;

    Command command = parser.parse(subject, start, end, description, location, isPublic,
        autoDecline);
    String result = "";
    try {
      result = command.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertEquals("Error: Start time is required.", result);
  }

  @Test
  public void testInvalidStartDate() {
    String subject = "Meeting";
    String start = "invalidDate";
    String end = "2025-04-10T11:00";
    String description = "Some description";
    String location = "Room 1";
    boolean isPublic = true;
    boolean autoDecline = false;

    Command command = parser.parse(subject, start, end, description, location, isPublic,
        autoDecline);
    String result = "";
    try {
      result = command.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }

    assertTrue(result.startsWith("Error parsing start date/time:"));
  }

  @Test
  public void testDefaultEndWhenEmpty() {

    String subject = "Meeting";
    String start = "2025-04-10T10:00";
    String end = "";
    String description = "Some description";
    String location = "Room 1";
    boolean isPublic = true;
    boolean autoDecline = false;

    Command command = parser.parse(subject, start, end, description, location, isPublic,
        autoDecline);
    String result = "";
    try {
      result = command.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }

    assertEquals("Event created: " + subject, result);
  }

}