package controller;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import model.CalendarManager;
import model.ICalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the GUIPrintEventsParser class. These will check that event printing commands are
 * properly parsed.
 */
public class GUIPrintEventsParserTest {

  private GUIPrintEventsParser parser;

  @Before
  public void setUp() throws Exception {

    ICalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);

    service.createCalendar("Default", "UTC");
    service.useCalendar("Default");

    parser = new GUIPrintEventsParser(service);
  }

  @Test
  public void testParseNullDate() throws Exception {

    Command cmd = parser.parse(null);
    String result = cmd.execute();
    assertEquals("Error: Date is required.", result);
  }

  @Test
  public void testParseValidDateWithNoEvents() throws Exception {

    LocalDate date = LocalDate.of(2025, 4, 10);
    Command cmd = parser.parse(date);
    String result = cmd.execute();

    assertEquals("", result);
  }

}