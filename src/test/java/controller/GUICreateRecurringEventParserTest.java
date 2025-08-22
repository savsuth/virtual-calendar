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
 * A test case to verify the Parsing of the {@link GUICreateRecurringEventParserTest} which parse
 * recurring events for the GUI.
 */
public class GUICreateRecurringEventParserTest {

  private GUICreateRecurringEventParser parser;

  @Before
  public void setUp() throws Exception {

    ICalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);
    service.createCalendar("ABC", "UTC");
    service.useCalendar("ABC");

    parser = new GUICreateRecurringEventParser(service);


  }

  @Test
  public void testValidRecurringCreationWithOccurrenceCount() {

    String subject = "WeeklyMeeting";
    String start = "2025-04-10T10:00";
    String end = "2025-04-10T11:00";
    String description = "Team sync";
    String location = "Conference Room";
    boolean isPublic = true;
    String recurrenceDays = "MWF";
    String occurrenceCount = "3";
    String recurrenceEndDate = "";
    boolean autoDecline = false;

    Command command = parser.parse(subject, start, end, description, location, isPublic,
        recurrenceDays, occurrenceCount, recurrenceEndDate, autoDecline);
    String result = "";
    try {
      result = command.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertEquals("Recurring event created: " + subject, result);
  }

  @Test
  public void testValidRecurringCreationWithRecurrenceEndDate() {

    String subject = "DailyRoutine";
    String start = "2025-04-10T08:00";
    String end = "2025-04-10T09:00";
    String description = "Morning workout";
    String location = "Gym";
    boolean isPublic = false;
    String recurrenceDays = "MTWRF";
    String occurrenceCount = "";
    String recurrenceEndDate = "2025-04-20";
    boolean autoDecline = false;

    Command command = parser.parse(subject, start, end, description, location, isPublic,
        recurrenceDays, occurrenceCount, recurrenceEndDate, autoDecline);
    String result = "";
    try {
      result = command.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertEquals("Recurring event created: " + subject, result);
  }

  @Test
  public void testErrorWhenNoOccurrenceCountOrRecurrenceEndProvided() {

    String subject = "InvalidEvent";
    String start = "2025-04-10T10:00";
    String end = "2025-04-10T11:00";
    String description = "Invalid case";
    String location = "Office";
    boolean isPublic = true;
    String recurrenceDays = "MWF";
    String occurrenceCount = "";
    String recurrenceEndDate = "";
    boolean autoDecline = false;

    Command command = parser.parse(subject, start, end, description, location, isPublic,
        recurrenceDays, occurrenceCount, recurrenceEndDate, autoDecline);
    String result = "";
    try {
      result = command.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertEquals("Error: Either occurrence count or recurrence end date must be provided.", result);
  }

  @Test
  public void testErrorWithNonNumericOccurrenceCount() {

    String subject = "NonNumericEvent";
    String start = "2025-04-10T10:00";
    String end = "2025-04-10T11:00";
    String description = "Test non numeric occurrence count";
    String location = "Office";
    boolean isPublic = true;
    String recurrenceDays = "MWF";
    String occurrenceCount = "abc";
    String recurrenceEndDate = "";
    boolean autoDecline = false;

    Command command = parser.parse(subject, start, end, description, location, isPublic,
        recurrenceDays, occurrenceCount, recurrenceEndDate, autoDecline);
    String result = "";
    try {
      result = command.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }

    assertEquals("Error: Occurrence count must be numeric.", result);
  }

  @Test
  public void testErrorWithEmptySubject() {

    String subject = "";
    String start = "2025-04-10T10:00";
    String end = "2025-04-10T11:00";
    String description = "Test empty subject";
    String location = "Office";
    boolean isPublic = true;
    String recurrenceDays = "MWF";
    String occurrenceCount = "3";
    String recurrenceEndDate = "";
    boolean autoDecline = false;

    Command command = parser.parse(subject, start, end, description, location, isPublic,
        recurrenceDays, occurrenceCount, recurrenceEndDate, autoDecline);
    String result = "";
    try {
      result = command.execute();
    } catch (Exception e) {
      fail("Execution threw an exception during empty subject test: " + e.getMessage());
    }
    assertEquals("Error: Subject cannot be empty.", result);
  }

  @Test
  public void testErrorWithEmptyStart() {

    String subject = "ValidSubject";
    String start = "";
    String end = "2025-04-10T11:00";
    String description = "Test empty start";
    String location = "Office";
    boolean isPublic = true;
    String recurrenceDays = "MWF";
    String occurrenceCount = "3";
    String recurrenceEndDate = "";
    boolean autoDecline = false;

    Command command = parser.parse(subject, start, end, description, location, isPublic,
        recurrenceDays, occurrenceCount, recurrenceEndDate, autoDecline);
    String result = "";
    try {
      result = command.execute();
    } catch (Exception e) {
      fail("Execution threw an exception during empty start test: " + e.getMessage());
    }
    assertEquals("Error: Start time is required.", result);
  }

  @Test
  public void testErrorWithEmptyRecurrenceDays() {

    String subject = "ValidSubject";
    String start = "2025-04-10T10:00";
    String end = "2025-04-10T11:00";
    String description = "Test empty recurrence days";
    String location = "Office";
    boolean isPublic = true;
    String recurrenceDays = "";
    String occurrenceCount = "3";
    String recurrenceEndDate = "";
    boolean autoDecline = false;

    Command command = parser.parse(subject, start, end, description, location, isPublic,
        recurrenceDays, occurrenceCount, recurrenceEndDate, autoDecline);
    String result = "";
    try {
      result = command.execute();
    } catch (Exception e) {
      fail("Execution threw an exception during empty recurrence days test: " + e.getMessage());
    }
    assertEquals("Error: Recurrence days are required.", result);
  }

}