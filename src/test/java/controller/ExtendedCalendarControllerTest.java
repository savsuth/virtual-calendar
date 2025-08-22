package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import model.CalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.Test;

/**
 * Tests for the {@link ExtendedCalendarControllerTest}.
 */

public class ExtendedCalendarControllerTest {

  @Test
  public void testExitCommand() {
    CalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);
    ExtendedCalendarController controller = new ExtendedCalendarController(service);
    String result = controller.processCommand("exit");
    assertEquals("exit", result);
  }

  @Test
  public void testUnknownCommand() {
    CalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);
    ExtendedCalendarController controller = new ExtendedCalendarController(service);
    String result = controller.processCommand("foobar");
    assertEquals("Unknown command.", result);
  }

  @Test
  public void testEmptyCommand() {
    CalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);
    ExtendedCalendarController controller = new ExtendedCalendarController(service);
    String result = controller.processCommand("");
    assertEquals("", result);
  }

  @Test
  public void testInvalidPrintCommand() {
    CalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);
    ExtendedCalendarController controller = new ExtendedCalendarController(service);
    String result = controller.processCommand("print events on not-a-date");
    assertTrue(result.toLowerCase().contains("invalid date format"));
  }
}
