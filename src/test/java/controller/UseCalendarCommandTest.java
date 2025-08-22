package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import model.CalendarContext;
import model.CalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.Test;

/**
 * JUnit Test Cases for UseCalendarCommandTest.
 */
public class UseCalendarCommandTest {

  @Test
  public void testUseValidCalendar() throws Exception {
    CalendarManager manager = new CalendarManager();
    CalendarContext context = new CalendarContext("MyCal", "Asia/Kolkata");
    manager.addCalendar(context);
    IMultiCalendarService service = new MultiCalendarService(manager);

    UseCalendarCommand cmd = new UseCalendarCommand(service, "MyCal");
    String result = cmd.execute();

    assertEquals("Using calendar: MyCal", result);
    assertEquals("MyCal", manager.getCurrentCalendar().getName());
  }


  @Test
  public void testUseInvalidCalendar() throws Exception {
    CalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);

    UseCalendarCommand cmd = new UseCalendarCommand(service, "GhostCal");
    String result = cmd.execute();

    assertTrue(result.toLowerCase().contains("not found"));
    assertNull(manager.getCurrentCalendar());
  }
}


