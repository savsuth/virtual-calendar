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
 * Unit tests for the CopyEventsBetweenCommand class. These tests verify the behavior of copying
 * events between calendars over a date range.
 */
public class CopyEventsBetweenCommandTest {

  @Test
  public void testExecuteCopiesRange() throws Exception {
    CalendarContext sourceCal = new CalendarContext("SemesterSpring2025", "America/New_York");
    CalendarContext targetCal = new CalendarContext("SemesterFall2025", "Asia/Kolkata");

    sourceCal.getCalendarService().addSingleEvent("Orientation",
        LocalDateTime.of(2025, 3, 1, 9, 0),
        LocalDateTime.of(2025, 3, 1, 10, 0),
        "Intro session", "Auditorium", true, true);

    CalendarManager manager = new CalendarManager();
    manager.addCalendar(sourceCal);
    manager.addCalendar(targetCal);
    manager.setCurrentCalendar("SemesterSpring2025");

    IMultiCalendarService service = new MultiCalendarService(manager);

    CopyEventsBetweenCommand command = new CopyEventsBetweenCommand(service, LocalDate.of(2025,
        3, 1),
        LocalDate.of(2025, 3, 2), "SemesterFall2025",
        LocalDate.of(2025, 6, 1)
    );

    String result = command.execute();
    assertTrue(result.toLowerCase().contains("copied"));
  }

  @Test
  public void testExecuteNoEventsInRange() throws Exception {
    CalendarContext sourceCal = new CalendarContext("SourceCalendar",
        "America/New_York");
    CalendarContext targetCal = new CalendarContext("TargetCalendar",
        "Asia/Kolkata");

    sourceCal.getCalendarService().addSingleEvent("Workshop",
        LocalDateTime.of(2025, 2, 15, 14, 0),
        LocalDateTime.of(2025, 2, 15, 15, 0),
        "Topic", "Hall", true, true);

    CalendarManager manager = new CalendarManager();
    manager.addCalendar(sourceCal);
    manager.addCalendar(targetCal);
    manager.setCurrentCalendar("SourceCalendar");

    IMultiCalendarService service = new MultiCalendarService(manager);

    CopyEventsBetweenCommand command = new CopyEventsBetweenCommand(service,
        LocalDate.of(2025, 3, 1),
        LocalDate.of(2025, 3, 2),
        "TargetCalendar",
        LocalDate.of(2025, 4, 1)
    );
    String result = command.execute();
    assertTrue(result.toLowerCase().contains("no events"));
  }


  @Test
  public void testCopyBetweenSameCalendar() throws Exception {
    CalendarContext calendar = new CalendarContext("SameCal", "Asia/Kolkata");

    calendar.getCalendarService().addSingleEvent("SelfEvent",
        LocalDateTime.of(2025, 3, 10, 10, 0),
        LocalDateTime.of(2025, 3, 10, 11, 0),
        "Internal", "Room", true, true);

    CalendarManager manager = new CalendarManager();
    manager.addCalendar(calendar);
    manager.setCurrentCalendar("SameCal");

    IMultiCalendarService service = new MultiCalendarService(manager);

    CopyEventsBetweenCommand cmd = new CopyEventsBetweenCommand(service,
        LocalDate.of(2025, 3, 10),
        LocalDate.of(2025, 3, 10),
        "SameCal",
        LocalDate.of(2025, 4, 10));
    String result = cmd.execute();
    assertTrue(result.toLowerCase().contains("copied"));
  }
}
