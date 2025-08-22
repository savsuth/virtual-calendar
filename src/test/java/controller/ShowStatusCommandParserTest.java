package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import model.CalendarModel;
import model.CalendarService;
import model.ICalendarService;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the ShowStatusCommandParser class.
 */
public class ShowStatusCommandParserTest {

  private ShowStatusCommandParser parser;

  /**
   * Sets up the test fixture before each test.
   */
  @Before
  public void setUp() {
    CalendarModel calendar = new CalendarModel();
    ICalendarService calendarService = new CalendarService(calendar);
    parser = new ShowStatusCommandParser(calendarService);
  }


  @Test
  public void testValidShowStatusCommand() throws Exception {
    String[] tokens = "show status on 2025-03-01T09:30".split("\\s+");
    Command cmd = parser.parse(tokens);
    String result = cmd.execute();
    assertTrue(result.equals("Busy") || result.equals("Available"));
  }


  @Test
  public void testInvalidShowCommandTooFewTokens() throws Exception {
    String[] tokens = "show status".split("\\s+");
    Command cmd = parser.parse(tokens);
    String result = cmd.execute();
    assertEquals("Invalid show command.", result);
  }


  @Test
  public void testInvalidShowCommandWrongKeywords() throws Exception {
    String[] tokens1 = "show stat on 2025-03-01T09:30".split("\\s+");
    Command cmd1 = parser.parse(tokens1);
    assertNotNull(cmd1);
    String result1 = cmd1.execute();
    assertEquals("Invalid show command.", result1);

    String[] tokens2 = "show status off 2025-03-01T09:30".split("\\s+");
    Command cmd2 = parser.parse(tokens2);
    assertNotNull(cmd2);
    String result2 = cmd2.execute();
    assertEquals("Invalid show command.", result2);
  }


  @Test
  public void testShowCommandInvalidDateTime() throws Exception {
    String[] tokens = "show status on invalidDate".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    String result = cmd.execute();
    assertTrue(result.startsWith("Error processing show command:"));
  }
}