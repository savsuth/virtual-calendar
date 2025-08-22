package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.Test;


/**
 * Unit tests for the EventPrinter class. These tests ensure event information is correctly printed
 * for given dates or ranges.
 */
public class EventPrinterTest {

  @Test
  public void testPrintEventsOnWithEvent() throws Exception {
    CalendarModel model = new CalendarModel();
    EventPrinter printer = new EventPrinter(model);
    SingleEvent event = new SingleEvent("TestEvent",
        LocalDateTime.of(2025, 5, 1, 9, 0),
        LocalDateTime.of(2025, 5, 1, 10, 0),
        "Description", "Room1", true);
    model.addEvent(event, false);
    String output = printer.printEventsOn(LocalDate.of(2025, 5, 1));
    assertTrue(output.contains("TestEvent"));
    assertTrue(output.contains("Room1"));
    assertTrue(output.contains("2025-05-01T09:00"));
  }

  @Test
  public void testPrintEventsRangeWithMultipleEvents() throws Exception {
    CalendarModel model = new CalendarModel();
    EventPrinter printer = new EventPrinter(model);
    SingleEvent event1 = new SingleEvent("Event1",
        LocalDateTime.of(2025, 5, 2, 8, 0),
        LocalDateTime.of(2025, 5, 2, 9, 0),
        "Desc", "Loc1", true);
    SingleEvent event2 = new SingleEvent("Event2",
        LocalDateTime.of(2025, 5, 2, 10, 0),
        LocalDateTime.of(2025, 5, 2, 11, 0),
        "Desc", "Loc2", true);
    model.addEvent(event1, false);
    model.addEvent(event2, false);
    String output = printer.printEventsRange(
        LocalDateTime.of(2025, 5, 2, 7, 0),
        LocalDateTime.of(2025, 5, 2, 12, 0));
    assertTrue(output.contains("Event1"));
    assertTrue(output.contains("Event2"));
    assertTrue(output.contains("2025-05-02T08:00"));
    assertTrue(output.contains("2025-05-02T10:00"));
  }

  @Test
  public void testPrintEventsOnEventWithNoLocation() throws Exception {
    CalendarModel model = new CalendarModel();
    EventPrinter printer = new EventPrinter(model);
    SingleEvent event = new SingleEvent("NoLocationEvent",
        LocalDateTime.of(2025, 5, 3, 14, 0),
        LocalDateTime.of(2025, 5, 3, 15, 0),
        "Desc", "", true);
    model.addEvent(event, false);
    String output = printer.printEventsOn(LocalDate.of(2025, 5, 3));
    assertTrue(output.contains("NoLocationEvent"));
    assertTrue(output.contains("2025-05-03T14:00"));
  }

  @Test
  public void testPrintEventsOnEmpty() throws Exception {
    CalendarModel model = new CalendarModel();
    EventPrinter printer = new EventPrinter(model);
    String output = printer.printEventsOn(LocalDate.of(2025, 5, 1));
    assertTrue(output.isEmpty());
    String[] lines = output.split("\n");
    assertEquals(1, lines.length);
  }
}
