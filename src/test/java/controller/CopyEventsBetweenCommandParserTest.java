package controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import model.CalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.Test;

/**
 * Test cases for the CopyEventsBetweenCommandParser class.
 */
public class CopyEventsBetweenCommandParserTest {

  @Test
  public void testValidParse() {
    IMultiCalendarService service = new MultiCalendarService(new CalendarManager());
    CopyEventsBetweenCommandParser parser = new CopyEventsBetweenCommandParser(service);
    String[] input = {"copy", "events", "between", "2025-03-01", "and", "2025-03-03",
        "--target", "TargetCal", "to", "2025-06-01"
    };
    Object cmd = parser.parse(input);
    assertTrue(cmd instanceof CopyEventsBetweenCommand);
  }

  @Test
  public void testInvalidParse() {
    IMultiCalendarService service = new MultiCalendarService(new CalendarManager());
    CopyEventsBetweenCommandParser parser = new CopyEventsBetweenCommandParser(service);
    String[] badInput = {"copy", "bad", "input"};
    Object cmd = parser.parse(badInput);
    assertFalse(cmd instanceof CopyEventsBetweenCommand);
  }


  @Test
  public void testInvalidTokenCount() throws Exception {
    IMultiCalendarService service = new MultiCalendarService(new CalendarManager());
    CopyEventsBetweenCommandParser parser = new CopyEventsBetweenCommandParser(service);

    // Test with 9 tokens instead of 10
    String[] input = {"copy", "events", "between", "2025-03-01", "and", "2025-03-03",
        "--target", "TargetCal", "to"};
    Command cmd = parser.parse(input);
    assertTrue("Should detect invalid format",
        cmd.execute().contains("Invalid copy events between command format"));
  }

  @Test
  public void testMissingEventsBetweenKeywords() throws Exception {
    IMultiCalendarService service = new MultiCalendarService(new CalendarManager());
    CopyEventsBetweenCommandParser parser = new CopyEventsBetweenCommandParser(service);

    // Missing "between" keyword
    String[] input = {"copy", "events", "from", "2025-03-01", "and", "2025-03-03",
        "--target", "TargetCal", "to", "2025-06-01"};
    Command cmd = parser.parse(input);
    assertTrue("Should detect invalid syntax",
        cmd.execute().contains("Invalid copy events between command syntax"));
  }

  @Test
  public void testMissingAndKeyword() throws Exception {
    IMultiCalendarService service = new MultiCalendarService(new CalendarManager());
    CopyEventsBetweenCommandParser parser = new CopyEventsBetweenCommandParser(service);

    // Missing "and" keyword
    String[] input = {"copy", "events", "between", "2025-03-01", "to", "2025-03-03",
        "--target", "TargetCal", "to", "2025-06-01"};
    Command cmd = parser.parse(input);
    assertTrue("Should detect missing 'and'",
        cmd.execute().contains("Missing 'and' in copy events between command"));
  }

  @Test
  public void testMissingTargetOption() throws Exception {
    IMultiCalendarService service = new MultiCalendarService(new CalendarManager());
    CopyEventsBetweenCommandParser parser = new CopyEventsBetweenCommandParser(service);

    // Missing --target
    String[] input = {"copy", "events", "between", "2025-03-01", "and", "2025-03-03",
        "--dest", "TargetCal", "to", "2025-06-01"};
    Command cmd = parser.parse(input);
    assertTrue("Should detect missing --target",
        cmd.execute().contains("Missing '--target' in copy events between command"));
  }

  @Test
  public void testMissingToKeyword() throws Exception {
    IMultiCalendarService service = new MultiCalendarService(new CalendarManager());
    CopyEventsBetweenCommandParser parser = new CopyEventsBetweenCommandParser(service);

    // Missing "to" keyword
    String[] input = {"copy", "events", "between", "2025-03-01", "and", "2025-03-03",
        "--target", "TargetCal", "at", "2025-06-01"};
    Command cmd = parser.parse(input);
    assertTrue("Should detect missing 'to'",
        cmd.execute().contains("Missing 'to' keyword in copy events between command"));
  }

  @Test
  public void testInvalidDateFormat() throws Exception {
    IMultiCalendarService service = new MultiCalendarService(new CalendarManager());
    CopyEventsBetweenCommandParser parser = new CopyEventsBetweenCommandParser(service);

    // Invalid date format
    String[] input = {"copy", "events", "between", "2025/03/01", "and", "2025-03-03",
        "--target", "TargetCal", "to", "2025-06-01"};
    Command cmd = parser.parse(input);
    assertTrue("Should handle date parse error",
        cmd.execute().contains("Error processing copy events between command"));
  }
}


