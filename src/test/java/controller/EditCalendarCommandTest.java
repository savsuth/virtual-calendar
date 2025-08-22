package controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import model.CalendarContext;
import model.CalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.Test;

/**
 * JUnit Test Case for {@link EditCalendarCommand}.
 */

public class EditCalendarCommandTest {

  @Test
  public void testEditNameSuccess() throws Exception {
    CalendarManager manager = new CalendarManager();
    CalendarContext context = new CalendarContext("OldName", "Asia/Kolkata");
    manager.addCalendar(context);
    IMultiCalendarService service = new MultiCalendarService(manager);

    EditCalendarCommand cmd = new EditCalendarCommand(service, "OldName",
        "name", "NewName");
    String result = cmd.execute();

    assertTrue(result.toLowerCase().contains("updated"));
    assertNotNull(manager.getCalendar("NewName"));
  }

  @Test
  public void testInvalidProperty() throws Exception {
    CalendarManager manager = new CalendarManager();
    CalendarContext context = new CalendarContext("OldName", "Asia/Kolkata");
    manager.addCalendar(context);
    IMultiCalendarService service = new MultiCalendarService(manager);

    EditCalendarCommand cmd = new EditCalendarCommand(service, "OldName", "invalid", "val");
    String result = cmd.execute();

    assertTrue(result.toLowerCase().contains("unknown calendar property"));
  }
}

