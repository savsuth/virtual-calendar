package controller;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import model.CalendarContext;
import model.CalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.Test;


/**
 * Unit test for the CopyEventCommand class. Verifies that an event can be successfully copied from
 * one calendar to another.
 */
public class CopyEventCommandTest {

  @Test
  public void testExecuteCopiesEvent() throws Exception {
    CalendarContext source = new CalendarContext("Source", "America/New_York");
    CalendarContext target = new CalendarContext("Target", "Asia/Kolkata");

    CalendarManager manager = new CalendarManager();
    manager.addCalendar(source);
    manager.addCalendar(target);
    manager.setCurrentCalendar("Source");

    IMultiCalendarService service = new MultiCalendarService(manager);

    source.getCalendarService().addSingleEvent("Test",
        LocalDateTime.of(2025, 2, 1, 10, 0),
        LocalDateTime.of(2025, 2, 1, 11, 0),
        "desc", "place", true, true);

    CopyEventCommand cmd = new CopyEventCommand(service, "Test",
        LocalDateTime.of(2025, 2, 1, 10, 0),
        "Target",
        LocalDateTime.of(2025, 2, 1, 20, 30));

    String result = cmd.execute();
    assertTrue(result.toLowerCase().contains("copied"));
  }
}