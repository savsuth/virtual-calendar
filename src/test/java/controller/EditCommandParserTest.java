package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import model.CalendarModel;
import model.CalendarService;
import model.ICalendarService;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Test Case for {@link EditCommandParser}.
 */

public class EditCommandParserTest {

  /**
   * Unit tests for the EditCommandParser class.
   */

  private EditCommandParser parser;

  /**
   * Sets up the test fixture before each test.
   */
  @Before
  public void setUp() {
    CalendarModel calendar = new CalendarModel();
    ICalendarService calendarService = new CalendarService(calendar);
    parser = new EditCommandParser(calendarService);
  }

  /**
   * Tests the SINGLE mode branch with a valid edit command.
   */
  @Test
  public void testEditEventSingleModeValid() {
    CalendarModel calendar = new CalendarModel();
    CalendarService service = new CalendarService(calendar);
    EditCommandParser parsera = new EditCommandParser(service);
    try {
      service.addSingleEvent("Meeting",
          LocalDateTime.of(2025, 3, 1, 9, 0),
          LocalDateTime.of(2025, 3, 1, 10, 0),
          "", "", true, false);
    } catch (Exception e) {
      fail(e.getMessage());
    }
    String[] tokens = ("edit event subject Meeting from 2025-03-01T09:00 to "
        + "2025-03-01T10:00 with TeamMeeting").split(
        "\\s+");
    Command cmd = parsera.parse(tokens);
    assertNotNull(cmd);
    try {
      String result = cmd.execute();
      assertTrue(result.contains("Edited event(s)"));
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * Tests that missing "from" in the edit event command returns the expected error message.
   */
  @Test
  public void testEditEventMissingFrom() throws Exception {
    String[] tokens = ("edit event subject Meeting 2025-03-01T09:00 to "
        + "2025-03-01T10:00 with TeamMeeting").split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull("Expected a command object even for invalid syntax", cmd);
    String result = cmd.execute();
    assertEquals("Expected 'from' in edit event command.", result);
  }

  /**
   * Tests the "edit events" branch in FROM mode.
   */
  @Test
  public void testEditEventsFromModeValid() throws Exception {
    CalendarModel calendar = new CalendarModel();
    CalendarService service = new CalendarService(calendar);
    EditCommandParser parsera = new EditCommandParser(service);

    Set<DayOfWeek> days = new HashSet<>();
    days.add(DayOfWeek.SATURDAY);
    service.addRecurringEvent(
        "Meeting",
        LocalDateTime.of(2025, 3, 1, 9, 0),
        LocalDateTime.of(2025, 3, 1, 10, 0),
        "desc",
        "loc",
        true,
        days,
        -1,
        LocalDate.of(2025, 3, 22),
        false
    );

    String[] tokens = ("edit events location Meeting from 2025-03-08T09:00 with NewLocation"
    ).split("\\s+");
    Command cmd = parsera.parse(tokens);
    assertNotNull(cmd);

    String result = cmd.execute();
    assertTrue(result.contains("Edited event(s)"));
  }


  /**
   * Tests the "edit events" branch in ALL mode.
   */
  @Test
  public void testEditEventsAllModeValid() throws Exception {
    CalendarModel calendar = new CalendarModel();
    CalendarService service = new CalendarService(calendar);
    EditCommandParser parser = new EditCommandParser(service);

    Set<DayOfWeek> days = new HashSet<>();
    days.add(DayOfWeek.SATURDAY);
    service.addRecurringEvent(
        "Meeting",
        LocalDateTime.of(2025, 3, 1, 9, 0),
        LocalDateTime.of(2025, 3, 1, 10, 0),
        "desc",
        "loc",
        true,
        days,
        3,
        null,
        false
    );

    String[] tokens = "edit events description Meeting with NewDescription".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);

    String result = cmd.execute();
    assertTrue(result.contains("Edited event(s)"));
  }

  /**
   * Tests missing "with" keyword returns the expected error message.
   */
  @Test
  public void testEditCommandMissingWith() throws Exception {
    String[] tokens = ("edit event subject Meeting from "
        + "2025-03-01T09:00 to 2025-03-01T10:00 TeamMeeting").split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull("Expected a command object even if 'with' is missing", cmd);
    String result = cmd.execute();
    assertEquals("Edit command missing 'with' and new value.", result);
  }

  /**
   * Tests an invalid date token in the edit command.
   */
  @Test
  public void testEditEventInvalidDate() throws Exception {
    String[] tokens = ("edit event subject Meeting from invalidDate "
        + "to 2025-03-01T10:00 with NewValue").split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull("Expected a command even with an invalid date", cmd);
    String result = cmd.execute();
    assertTrue(result.startsWith("Error processing edit command:"));
  }

  /**
   * Tests missing "with" keyword in SINGLE mode.
   */
  @Test
  public void testEditCommandMissingWith_SingleMode() throws Exception {
    String[] tokens = ("edit event subject Meeting from "
        + "2025-03-01T09:00 to 2025-03-01T10:00 TeamMeeting").split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull("Expected a command object even if 'with' is missing", cmd);
    String result = cmd.execute();
    assertEquals("Edit command missing 'with' and new value.", result);
  }

  /**
   * Tests missing "with" keyword in ALL mode.
   */
  @Test
  public void testEditCommandMissingWith_AllMode() throws Exception {
    String[] tokens = "edit events subject Meeting".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    String result = cmd.execute();
    assertEquals("Edit command missing 'with' and new value.", result);
  }

  /**
   * Tests an invalid edit command type (tokens[1] not "event" or "events").
   */
  @Test
  public void testInvalidEditCommandType() throws Exception {
    String[] tokens = "edit something".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    String result = cmd.execute();
    assertEquals("Invalid edit command.", result);
  }

  /**
   * Tests invalid token after edit command.
   */
  @Test
  public void testInvalidTokenAfterEdit() throws Exception {
    String[] tokens = "edit wrongCommand".split("\\s+");
    Command cmd = parser.parse(tokens);
    String result = cmd.execute();
    assertEquals("Invalid edit command.", result);
  }

  /**
   * Tests valid SINGLE mode command without auto-decline.
   */
  @Test
  public void testEditEventSingleModeNoAutoDecline() throws Exception {
    CalendarModel calendar = new CalendarModel();
    CalendarService service = new CalendarService(calendar);
    EditCommandParser parser = new EditCommandParser(service);

    service.addSingleEvent("Meeting",
        LocalDateTime.of(2025, 3, 1, 9, 0),
        LocalDateTime.of(2025, 3, 1, 10, 0),
        "", "", true, false);

    String[] tokens = ("edit event subject Meeting from 2025-03-01T09:00 to "
        + "2025-03-01T10:00 with TeamMeeting")
        .split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);

    String result = cmd.execute();
    assertTrue(result.contains("Edited event(s)"));
  }


  @Test
  public void testEditEventSingleModeWithAutoDecline() throws Exception {
    CalendarModel calendar = new CalendarModel();
    CalendarService service = new CalendarService(calendar);
    EditCommandParser parser = new EditCommandParser(service);

    service.addSingleEvent("Meeting",
        LocalDateTime.of(2025, 3, 1, 9, 0),
        LocalDateTime.of(2025, 3, 1, 10, 0),
        "", "", true, false);

    String[] tokens = ("edit event --autoDecline subject Meeting "
        + "from 2025-03-01T09:00 to 2025-03-01T10:00 with TeamMeeting").split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);

    String result = cmd.execute();
    assertTrue(result.contains("Edited event(s)"));
  }


  /**
   * Tests the "edit events" branch in ALL mode without auto-decline.
   */
  @Test
  public void testEditEventsAllModeNoAutoDecline() throws Exception {
    CalendarModel calendar = new CalendarModel();
    CalendarService service = new CalendarService(calendar);
    EditCommandParser parser = new EditCommandParser(service);

    Set<DayOfWeek> days = new HashSet<>();
    days.add(DayOfWeek.SATURDAY);
    service.addRecurringEvent("Meeting",
        LocalDateTime.of(2025, 3, 1, 9, 0),
        LocalDateTime.of(2025, 3, 1, 10, 0),
        "", "",
        true,
        days,
        3,
        null,
        false);

    String[] tokens = "edit events subject Meeting with FinalMeeting".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);

    String result = cmd.execute();
    assertTrue(result.contains("Edited event(s)"));
  }


  @Test
  public void testEditCommandMissingFrom() throws Exception {
    String[] tokens = "edit event subject Meeting TeamMeeting".split("\\s+");
    Command cmd = parser.parse(tokens);
    String result = cmd.execute();
    assertEquals("Expected 'from' in edit event command.", result);
  }


  @Test
  public void testEditEventsAllModeWithAutoDecline() throws Exception {
    CalendarModel calendar = new CalendarModel();
    CalendarService service = new CalendarService(calendar);
    EditCommandParser parser = new EditCommandParser(service);

    Set<DayOfWeek> days = new HashSet<>();
    days.add(DayOfWeek.SATURDAY);

    service.addRecurringEvent(
        "Meeting",
        LocalDateTime.of(2025, 3, 1, 9, 0),
        LocalDateTime.of(2025, 3, 1, 10, 0),
        "desc",
        "loc",
        true,
        days,
        -1,
        LocalDate.of(2025, 3, 29),
        false
    );

    String[] tokens = "edit events --autoDecline description Meeting with NewDesc".split("\\s+");
    Command cmd = parser.parse(tokens);
    String result = cmd.execute();

    assertTrue(result.contains("Edited event(s)"));
  }


  /**
   * Tests the "edit events" branch (FROM mode) with an invalid date token. Expected syntax: "edit
   * events location Meeting from invalidDate with NewLocation"
   */
  @Test
  public void testEditEventsFromModeInvalidDate() throws Exception {
    String[] tokens = ("edit events location Meeting "
        + "from invalidDate with NewLocation").split("\\s+");
    Command cmd = parser.parse(tokens);
    String result = cmd.execute();
    assertTrue(result.startsWith("Error processing edit command:"));
  }

  @Test
  public void testEditCommandTooShort() throws Exception {
    String[] tokens = "edit event".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull("Command should be created even for too-short input", cmd);
    String result = cmd.execute();
    assertTrue(result.contains("Error processing edit command:"));
  }

  /**
   * Tests missing "with" keyword in the "events" branch.
   */
  @Test
  public void testEditEventsMissingWith() throws Exception {
    String[] tokens = "edit events subject Meeting".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull("Even if 'with' is missing, a command object is created", cmd);
    String result = cmd.execute();
    assertEquals("Edit command missing 'with' and new value.", result);
  }

  /**
   * Tests "edit events" branch in FROM mode with missing "with" or new value.
   */
  @Test
  public void testEditEventsFromNoWithOrValue() throws Exception {
    String[] tokens = "edit events subject Meeting from 2025-03-01T09:00".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    String result = cmd.execute();
    assertEquals("Edit command missing 'with' and new value.", result);
  }

  /**
   * Tests "edit events" branch in FROM mode with "with" keyword but no value.
   */
  @Test
  public void testEditEventsFromModeMissingValueAfterWith() throws Exception {
    String[] tokens = "edit events subject Meeting from 2025-03-01T09:00 with".split("\\s+");
    Command cmd = parser.parse(tokens);
    String result = cmd.execute();
    assertEquals("Edit command missing 'with' and new value.", result);
  }

  @Test
  public void testAutoDeclineAsLastToken() throws Exception {
    String[] tokens = "edit event --autoDecline".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertTrue(cmd.execute().contains("Error processing edit command"));
  }

  @Test
  public void testMissingDateTimeAfterFrom() throws Exception {
    String[] tokens = "edit event subject Meeting from".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertTrue(cmd.execute().contains("Error processing edit command"));
  }

  @Test
  public void testWithAtLastPosition() throws Exception {
    String[] tokens = "edit events subject Meeting with".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertEquals("Edit command missing 'with' and new value.", cmd.execute());
  }

  @Test
  public void testInvalidCommandAfterEditEvents() throws Exception {
    String[] tokens = "edit events invalid".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertTrue(cmd.execute().contains("Error processing edit command"));
  }

}