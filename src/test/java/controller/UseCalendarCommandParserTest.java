package controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import model.CalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.Test;

/**
 * Test Cases for {@link UseCalendarCommandParser}.
 */
public class UseCalendarCommandParserTest {

  @Test
  public void testValidCommandParse() {
    IMultiCalendarService service = new MultiCalendarService(new CalendarManager());
    UseCalendarCommandParser parser = new UseCalendarCommandParser(service);

    String[] input = {"use", "calendar", "--name", "WorkCal"};
    Object command = parser.parse(input);

    assertNotNull(command);
    assertTrue(command instanceof UseCalendarCommand);
  }

  @Test
  public void testInvalidCommandMissingFlag() {
    IMultiCalendarService service = new MultiCalendarService(new CalendarManager());
    UseCalendarCommandParser parser = new UseCalendarCommandParser(service);

    String[] input = {"use", "calendar", "WorkCal"};
    Object command = parser.parse(input);

    assertFalse(command instanceof UseCalendarCommand);
  }

  @Test
  public void testInvalidCommandTooShort() {
    IMultiCalendarService service = new MultiCalendarService(new CalendarManager());
    UseCalendarCommandParser parser = new UseCalendarCommandParser(service);

    String[] input = {"use"};
    Object command = parser.parse(input);

    assertFalse(command instanceof UseCalendarCommand);
  }
}

