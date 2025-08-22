package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for CSVExporter. These tests verify the behavior of the exporter under many
 * scenarios.
 */
public class CSVExporterTest {

  private final String testFile = "test_calendar.csv";
  private ICalendarModel calendar;

  @Before
  public void setUp() throws InvalidDateException, EventConflictException {
    calendar = new CalendarModel();
  }

  @After
  public void tearDown() {
    File file = new File(testFile);
    if (file.exists()) {
      file.delete();
    }
  }

  @Test
  public void testExportCSVWithSingleEvent()
      throws IOException, InvalidDateException, EventConflictException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event = new SingleEvent("Meeting", start, end, "desc", "Room 101", true);
    calendar.addEvent(event, false);

    CSVExporter exporter = new CSVExporter();
    String filePath = exporter.export(calendar, testFile);
    File csvFile = new File(filePath);
    assertTrue("CSV file should exist.", csvFile.exists());

    BufferedReader reader = new BufferedReader(new FileReader(csvFile));
    String header = reader.readLine();

    assertEquals(
        "Subject,Start Date,Start Time,End Date,End Time,AllDayEvent,Description,Location,Private",
        header);
    String dataRow = reader.readLine();
    assertNotNull("There should be a data row for the event.", dataRow);
    String[] parts = dataRow.split(",");
    assertEquals("Meeting", parts[0]);
    assertEquals(start.toLocalDate().toString(), parts[1]);
    assertEquals(start.toLocalTime().toString(), parts[2]);
    assertEquals(end.toLocalDate().toString(), parts[3]);
    assertEquals(end.toLocalTime().toString(), parts[4]);

    assertEquals("false", parts[5]);
    assertEquals("desc", parts[6]);
    assertEquals("Room 101", parts[7]);

    assertEquals("false", parts[8]);
    reader.close();
  }

  @Test
  public void testExportCSVWithRecurringEvent()
      throws IOException, InvalidDateException, EventConflictException {

    LocalDateTime start = LocalDateTime.of(2025, 3, 3, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 3, 9, 15);
    Set<DayOfWeek> days = new HashSet<>();
    days.add(DayOfWeek.MONDAY);
    RecurringEvent recurring = new RecurringEvent("Standup", start, end, "", "", true, days, 2,
        null);
    calendar.addEvent(recurring, false);

    CSVExporter exporter = new CSVExporter();
    String filePath = exporter.export(calendar, testFile);
    File csvFile = new File(filePath);
    assertTrue("CSV file should exist.", csvFile.exists());

    BufferedReader reader = new BufferedReader(new FileReader(csvFile));
    String header = reader.readLine();
    assertEquals(
        "Subject,Start Date,Start Time,End Date,End Time,AllDayEvent,Description,Location,Private",
        header);
    String row1 = reader.readLine();
    String row2 = reader.readLine();
    assertNotNull("First occurrence row should not be null.", row1);
    assertNotNull("Second occurrence row should not be null.", row2);
    assertNull("There should be no more than two data rows.", reader.readLine());
    reader.close();
  }

  @Test
  public void testExportCSVWithEmptyCalendar() throws IOException {
    CSVExporter exporter = new CSVExporter();
    String filePath = exporter.export(calendar, testFile);
    File csvFile = new File(filePath);
    assertTrue("CSV file should exist.", csvFile.exists());

    BufferedReader reader = new BufferedReader(new FileReader(csvFile));
    String header = reader.readLine();
    assertEquals(
        "Subject,Start Date,Start Time,End Date,End Time,AllDayEvent,Description,Location,Private",
        header);
    String data = reader.readLine();
    assertNull("There should be no data rows when the calendar is empty.", data);
    reader.close();
  }

  @Test
  public void testReturnFilePath()
      throws IOException, InvalidDateException, EventConflictException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event = new SingleEvent("Meeting", start, end, "", "", true);
    calendar.addEvent(event, false);

    CSVExporter exporter = new CSVExporter();
    String filePath = exporter.export(calendar, testFile);
    assertNotNull("File path returned should not be null.", filePath);
    assertTrue("File path should contain the test file name.", filePath.contains(testFile));
  }

}