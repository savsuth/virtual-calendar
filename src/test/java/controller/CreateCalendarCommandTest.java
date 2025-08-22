package controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import model.CalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.Test;

/**
 * Tests for the CreateCalendarCommand class.
 */
public class CreateCalendarCommandTest {

  @Test
  public void testCreateValidCalendar() throws Exception {
    CalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);

    CreateCalendarCommand cmd = new CreateCalendarCommand(service, "NewCal", "Asia/Kolkata");
    String result = cmd.execute();

    assertTrue(result.contains("created"));
    assertNotNull(manager.getCalendar("NewCal"));
  }

  @Test
  public void testDuplicateCalendarFails() throws Exception {
    CalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);
    service.createCalendar("NewCal", "Asia/Kolkata");

    CreateCalendarCommand cmd = new CreateCalendarCommand(service, "NewCal",
        "Asia/Kolkata");
    String result = cmd.execute();
    assertTrue(result.toLowerCase().contains("failed"));
  }
}

