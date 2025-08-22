package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import model.CalendarManager;
import model.ICalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.Before;
import org.junit.Test;
import view.ICalendarView;

/**
 * Tests for the {@link GUIController} class.
 */
public class GUIControllerTest {

  private GUIController controller;

  /**
   * Initializes the test controller and sets up a default calendar for use.
   */
  @Before
  public void setUp() throws Exception {
    ICalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);
    ICalendarView view = new ICalendarView() {
      @Override
      public void start() {
        // View is needed by CalendarFactory.
        // Cannot use CalendarGUI here(pitest doesn't work if I do).
      }

      @Override
      public void setController(IGUIController controller) throws Exception {
        // Interface necessitates to use this methods.
      }
    };
    controller = new GUIController(service, view);

    controller.createCalendar("Default", "UTC");
    controller.useCalendar("Default");
  }

  @Test
  public void useCalendar() throws Exception {

    String createResult = controller.createCalendar("TestCalendar", "UTC");
    assertEquals("Calendar created: TestCalendar", createResult);

    String result = controller.useCalendar("TestCalendar");
    assertEquals("Using calendar: TestCalendar", result);
  }

  @Test
  public void createCalendar() {

    String result = controller.createCalendar("MyCalendar", "Europe/London");
    assertEquals("Calendar created: MyCalendar", result);

    String duplicateResult = controller.createCalendar("MyCalendar", "Europe/London");
    assertEquals("Calendar creation failed: Duplicate calendar name.", duplicateResult);
  }

  @Test
  public void createSingleEvent() {

    String result = controller.createSingleEvent(
        "Meeting",
        "2025-04-10T10:00",
        "2025-04-10T11:00",
        "Team meeting",
        "Conference Room",
        true,
        false);
    assertEquals("Event created: Meeting", result);
  }

  @Test
  public void createRecurringEvent() {

    String result = controller.createRecurringEvent(
        "WeeklyMeeting",
        "2025-04-10T10:00",
        "2025-04-10T11:00",
        "Recurring team sync",
        "Board Room",
        true,
        "MWF",
        "3",
        "",
        false);
    assertEquals("Recurring event created: WeeklyMeeting", result);
  }

  @Test
  public void editEvent() {

    String createResult = controller.createSingleEvent(
        "Meeting",
        "2025-04-10T10:00",
        "2025-04-10T11:00",
        "Discuss project",
        "Office",
        true,
        false);
    assertEquals("Event created: Meeting", createResult);

    String editResult = controller.editEvent(
        "Meeting",
        "2025-04-10T10:00",
        "subject",
        "UpdatedMeeting",
        "SINGLE");

    assertTrue(editResult.contains("Edited event(s) 'Meeting': subject changed to UpdatedMeeting"));
  }

  @Test
  public void printEventsOn() {

    controller.createSingleEvent(
        "Lunch",
        "2025-04-11T12:00",
        "2025-04-11T13:00",
        "Lunch with client",
        "Cafe",
        true,
        false);

    LocalDate date = LocalDate.parse("2025-04-11", DateTimeFormatter.ISO_LOCAL_DATE);
    String output = controller.printEventsOn(date);
    assertTrue(output.contains("Events on 2025-04-11:"));
    assertTrue(output.contains("- Lunch at Cafe 2025-04-11T12:00"));
  }

  @Test
  public void exportCalendar() {

    controller.createSingleEvent(
        "ExportMeeting",
        "2025-04-12T09:00",
        "2025-04-12T10:00",
        "Export test",
        "Room 1",
        true,
        false);

    String result = controller.exportCalendar("testCalendar.csv", "csv");

    assertTrue(result.contains("Calendar exported to:"));
  }

  @Test
  public void importCalendar() throws Exception {

    File tempFile = File.createTempFile("testCalendar", ".csv");
    tempFile.deleteOnExit();
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
      writer.write(
          "Subject,Start Date,Start Time,End Date,End Time,"
              + "AllDayEvent,Description,Location,Private");
      writer.newLine();

      writer.write("ImportedEvent,2025-04-10,09:00,2025-04-10,10:00,"
          + "false,ImpDesc,ImpLoc,false");
      writer.newLine();
    }

    String importResult = controller.importCalendar(tempFile.getAbsolutePath());
    assertTrue(importResult.contains("Imported"));
  }

  @Test
  public void editCalendar() {

    controller.createCalendar("MyCalendar", "UTC");

    String result = controller.editCalendar("MyCalendar", "name", "UpdatedMyCalendar");
    assertEquals("Calendar name updated to: UpdatedMyCalendar", result);

    String tzResult = controller.editCalendar("UpdatedMyCalendar", "timezone", "America/New_York");
    assertEquals("Calendar timezone updated to: America/New_York", tzResult);
  }

  @Test
  public void getCurrentCalendarNameAndZone() throws Exception {

    controller.createCalendar("TestCal", "Asia/Tokyo");
    controller.useCalendar("TestCal");
    String[] info = controller.getCurrentCalendarNameAndZone();
    assertNotNull(info);
    assertEquals(2, info.length);
    assertEquals("TestCal", info[0]);

    assertTrue(info[1].contains("Asia/Tokyo"));
  }

  @Test
  public void start() {
    try {
      controller.start();
    } catch (Exception ex) {
      fail("Just for coverage, this should not throw any exceptions " + ex.getMessage());
    }
  }


  @Test
  public void exportWithUnsupportedFormat() {
    String result = controller.exportCalendar("test.xyz", "xyz");
    assertTrue(result.contains("Error exporting calendar"));
  }

  @Test
  public void editCalendarInvalidProperty() {
    String result = controller.editCalendar("Default", "color", "blue");
    assertEquals("Unknown calendar property: color", result);
  }


}