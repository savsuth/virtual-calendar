package controller;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import model.CalendarModel;
import model.CalendarService;
import model.ICalendarService;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Test Cases for PrintEventsCommandTest.
 */
public class PrintEventsCommandTest {

  ICalendarService calendar;

  @Before
  public void setUp() throws Exception {
    CalendarModel calendara = new CalendarModel();
    calendar = new CalendarService(calendara);
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    calendar.addSingleEvent("Meeting", start, end, "", "", true, false);
  }

  /**
   * Tests that the PrintEventsCommand correctly prints events for a given date.
   */
  @Test
  public void testPrintEvents() throws Exception {
    LocalDate date = LocalDate.of(2025, 3, 1);
    PrintEventsCommand cmd = new PrintEventsCommand(calendar, date);
    String output = cmd.execute();
    assertTrue(output.contains("Meeting"));
    assertTrue(output.contains("2025-03-01T09:00"));
  }
}