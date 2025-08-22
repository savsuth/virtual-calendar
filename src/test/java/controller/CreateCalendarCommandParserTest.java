package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import model.CalendarManager;
import model.MultiCalendarService;
import org.junit.Test;

/**
 * Tests for the CreateCalendarCommandParser class.
 */
public class CreateCalendarCommandParserTest {

  @Test
  public void testValidParse() {
    CreateCalendarCommandParser parser =
        new CreateCalendarCommandParser(new MultiCalendarService(new CalendarManager()));
    String[] input = {"create", "calendar", "--name", "Work", "--timezone", "Asia/Kolkata"};

    Object result = parser.parse(input);
    assertTrue(result instanceof CreateCalendarCommand);
  }

  @Test
  public void testMissingTimezoneFlagButHasValue() {
    CreateCalendarCommandParser parser =
        new CreateCalendarCommandParser(new MultiCalendarService(new CalendarManager()));
    String[] input = {"create", "calendar", "--name", "MyCal", "Asia/Kolkata"};

    Object result = parser.parse(input);
    assertFalse(result instanceof CreateCalendarCommand);
  }

  @Test
  public void testInvalidTokenLength() throws Exception {
    CreateCalendarCommandParser parser = new CreateCalendarCommandParser(
        new MultiCalendarService(new CalendarManager()));
    String[] shortInput = {"create", "calendar", "--name", "Work"};
    Command cmd = parser.parse(shortInput);
    assertEquals("Invalid create calendar command.", cmd.execute());
  }

  @Test
  public void testMissingTimezoneFlagReturnsSpecificError() throws Exception {
    CreateCalendarCommandParser parser = new CreateCalendarCommandParser(
        new MultiCalendarService(new CalendarManager()));
    String[] input = {"create", "calendar", "--name", "MyCal", "UTC"};
    Command cmd = parser.parse(input);
    assertEquals("Invalid create calendar command.", cmd.execute());
  }

  @Test
  public void testValidCommandReturnsNonNull() {
    CreateCalendarCommandParser parser = new CreateCalendarCommandParser(
        new MultiCalendarService(new CalendarManager()));
    String[] validInput = {"create", "calendar", "--name", "Home", "--timezone", "Europe/Paris"};
    Command cmd = parser.parse(validInput);
    assertNotNull(cmd);
  }


}

