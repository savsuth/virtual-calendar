package controller;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import model.CalendarContext;
import model.CalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.Test;

/**
 * JUnit Test Cases for UseCalendarCommandTest.
 */
public class CopyEventsOnCommandTest {

  @Test
  public void testCopyEventsOnDate() throws Exception {
    CalendarContext source = new CalendarContext("CourseCal", "America/New_York");
    CalendarContext target = new CalendarContext("BackupCal", "Asia/Kolkata");

    source.getCalendarService().addSingleEvent("Exam",
        LocalDateTime.of(2025, 4, 10, 9, 0),
        LocalDateTime.of(2025, 4, 10, 10, 0),
        "", "", true, true);

    CalendarManager m = new CalendarManager();
    m.addCalendar(source);
    m.addCalendar(target);
    m.setCurrentCalendar("CourseCal");
    IMultiCalendarService service = new MultiCalendarService(m);

    CopyEventsOnCommand cmd = new CopyEventsOnCommand(service, LocalDate.of(2025,
        4, 10), "BackupCal",
        LocalDate.of(2025, 6, 10)
    );

    assertTrue(cmd.execute().toLowerCase().contains("copied"));
  }


  @Test
  public void testCopyRecurringInstance() throws Exception {
    CalendarContext source = new CalendarContext("WeeklyCal", "America/New_York");
    CalendarContext target = new CalendarContext("RepeatTarget", "Asia/Kolkata");

    source.getCalendarService().addRecurringEvent("Yoga",
        LocalDateTime.of(2025, 5, 7, 7, 0),
        LocalDateTime.of(2025, 5, 7, 8, 0),
        "Morning", "Studio", true,
        java.util.Collections.singleton(java.time.DayOfWeek.WEDNESDAY),
        1, LocalDate.of(2025, 5, 21), true);

    CalendarManager m = new CalendarManager();
    m.addCalendar(source);
    m.addCalendar(target);
    m.setCurrentCalendar("WeeklyCal");
    IMultiCalendarService service = new MultiCalendarService(m);

    CopyEventsOnCommand command = new CopyEventsOnCommand(service,
        LocalDate.of(2025, 5, 7), "RepeatTarget",
        LocalDate.of(2025, 6, 7)
    );
    String result = command.execute();
    assertTrue(result.toLowerCase().contains("copied"));
  }
}

