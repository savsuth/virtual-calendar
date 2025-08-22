package controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import model.CalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.Test;

/**
 * Tests the CopyEventsOnCommandParser.
 */
public class CopyEventsOnCommandParserTest {

  @Test
  public void testValidParse() {
    IMultiCalendarService service = new MultiCalendarService(new CalendarManager());
    CopyEventsOnCommandParser parser = new CopyEventsOnCommandParser(service);
    String[] input = {"copy", "events", "on", "2025-02-01", "--target", "CalX", "to", "2025-03-01"};
    Object cmd = parser.parse(input);
    assertTrue("Expected a CopyEventsOnCommand instance", cmd instanceof CopyEventsOnCommand);
  }

  @Test
  public void testInvalidParse() {
    IMultiCalendarService service = new MultiCalendarService(new CalendarManager());
    CopyEventsOnCommandParser parser = new CopyEventsOnCommandParser(service);
    String[] badInput = {"copy", "invalid", "command"};
    Object cmd = parser.parse(badInput);
    assertFalse("Expected the parser not to produce a valid CopyEventsOnCommand",
        cmd instanceof CopyEventsOnCommand);
  }
}
