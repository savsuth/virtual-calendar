package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import model.CalendarModel;
import model.CalendarService;
import model.ICalendarService;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Test Case for {@link HeadCommandParser}, which delegates the arguments to other dedicated
 * parsers based on the arguments provided.
 */

public class HeadCommandParserTest {

  private HeadCommandParser parser;

  @Before
  public void setUp() {
    CalendarModel calendar = new CalendarModel();
    ICalendarService service = new CalendarService(calendar);
    parser = new HeadCommandParser(service);
  }

  @Test
  public void testUnknownCommand() throws Exception {
    Command cmd = parser.parse("eprnig command");
    String result = cmd.execute();
    assertEquals("Unknown command.", result);
  }

  @Test
  public void testErrorProcessingInParse() throws Exception {
    Command cmd = parser.parse("create event Meeting from invalidDate to 2025-03-01T10:00");
    String result = cmd.execute();
    assertTrue(result.startsWith("Error processing create command:"));
  }

  @Test
  public void testEmptyInputReturnsNull() {
    Command cmd = parser.parse("    ");
    assertNull(cmd);
  }

  @Test
  public void testNullInputReturnsNull() {
    Command cmd = parser.parse(null);
    assertNull(cmd);
  }

}