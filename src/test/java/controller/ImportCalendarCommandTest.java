package controller;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import model.CalendarManager;
import model.ICalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the ImportCalendarCommand class.
 */
public class ImportCalendarCommandTest {

  private IMultiCalendarService service;
  private File tempFile;
  private String tempFilePath;

  /**
   * Prepares the calendar system and a temporary CSV file with a valid row before each test.
   */
  @Before
  public void setUp() throws Exception {

    ICalendarManager manager = new CalendarManager();
    service = new MultiCalendarService(manager);

    service.createCalendar("Default", "UTC");
    service.useCalendar("Default");

    tempFile = File.createTempFile("testImport", ".csv");
    tempFilePath = tempFile.getAbsolutePath();
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

      writer.write(
          "Subject,Start Date,Start Time,End Date,"
              + "End Time,AllDayEvent,Description,Location,Private");
      writer.newLine();

      writer.write("ImportedEvent,2025-04-10,09:00,2025-04-10,10:00,false,Desc,Loc,false");
      writer.newLine();
    }
  }

  @After
  public void tearDown() throws Exception {

    if (tempFile != null && tempFile.exists()) {
      tempFile.delete();
    }
  }

  @Test
  public void testSuccessfulImport() {

    ImportCalendarCommand command = new ImportCalendarCommand(service, "csv", tempFilePath);
    String result = "";
    try {
      result = command.execute();
    } catch (Exception e) {
      fail("Command execution threw an exception: " + e.getMessage());
    }

    assertTrue(
        result.toLowerCase().contains("imported"));
  }

  @Test
  public void testImportFileNotFound() {

    String nonExistentPath = Paths.get(System.getProperty("user.dir"), "nonexistentfile.csv")
        .toString();
    ImportCalendarCommand command = new ImportCalendarCommand(service, "csv", nonExistentPath);
    String result = "";
    try {
      result = command.execute();
    } catch (Exception e) {
      fail("Command execution threw an exception: " + e.getMessage());
    }

    assertTrue(
        result.startsWith("Import failed:"));
  }
}