package controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import model.CalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.Test;


/**
 * Unit tests for the CopyEventCommandParser class.
 */
public class CopyEventCommandParserTest {

  @Test
  public void testValidParse() {
    CalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);
    CopyEventCommandParser parser = new CopyEventCommandParser(service);

    String[] input = {"copy", "event", "Meeting", "on", "2025-02-01T10:00", "--target",
        "Cal2", "to", "2025-02-10T15:00"};
    assertTrue(parser.parse(input) instanceof CopyEventCommand);
  }

  @Test
  public void testInvalidParseReturnsFallback() {
    CalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);
    CopyEventCommandParser parser = new CopyEventCommandParser(service);

    String[] badInput = {"not", "a", "copy", "command"};
    Object result = parser.parse(badInput);
    assertFalse(result instanceof CopyEventCommand);
  }

  @Test
  public void testInvalidTokenCount() throws Exception {
    CalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);
    CopyEventCommandParser parser = new CopyEventCommandParser(service);

    // Test with 8 tokens instead of required 9
    String[] input = {"copy", "event", "Meeting", "on", "2025-02-01T10:00", "--target", "Cal2",
        "to"};
    Command cmd = parser.parse(input);
    assertTrue("Should return format error",
        cmd.execute().contains("Invalid copy event command format"));
  }

  @Test
  public void testMissingEventKeyword() throws Exception {
    CalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);
    CopyEventCommandParser parser = new CopyEventCommandParser(service);

    // Missing "event" keyword
    String[] input = {"copy", "meeting", "Meeting", "on", "2025-02-01T10:00", "--target", "Cal2",
        "to", "2025-02-10T15:00"};
    Command cmd = parser.parse(input);
    assertTrue("Should detect missing event keyword",
        cmd.execute().contains("missing 'event' keyword"));
  }

  @Test
  public void testMissingOnKeyword() throws Exception {
    CalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);
    CopyEventCommandParser parser = new CopyEventCommandParser(service);

    // Missing "on" keyword
    String[] input = {"copy", "event", "Meeting", "at", "2025-02-01T10:00", "--target", "Cal2",
        "to", "2025-02-10T15:00"};
    Command cmd = parser.parse(input);
    assertTrue("Should detect missing 'on' keyword",
        cmd.execute().contains("missing 'on' keyword"));
  }

  @Test
  public void testMissingTargetOption() throws Exception {
    CalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);
    CopyEventCommandParser parser = new CopyEventCommandParser(service);

    // Missing --target option
    String[] input = {"copy", "event", "Meeting", "on", "2025-02-01T10:00", "--dest", "Cal2", "to",
        "2025-02-10T15:00"};
    Command cmd = parser.parse(input);
    assertTrue("Should detect missing --target",
        cmd.execute().contains("missing '--target' option"));
  }

  @Test
  public void testMissingToKeyword() throws Exception {
    CalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);
    CopyEventCommandParser parser = new CopyEventCommandParser(service);

    // Missing "to" keyword
    String[] input = {"copy", "event", "Meeting", "on", "2025-02-01T10:00", "--target", "Cal2",
        "at", "2025-02-10T15:00"};
    Command cmd = parser.parse(input);
    assertTrue("Should detect missing 'to' keyword",
        cmd.execute().contains("missing 'to' keyword"));
  }
}