package controller;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.time.LocalDateTime;
import model.CalendarModel;
import model.CalendarService;
import model.ICalendarService;
import org.junit.Test;

/**
 * JUnit Test Case for {@link ExportCalendarCommandTest}. Verifies that the CSV are exported and its
 * feedback string is returned.
 */

public class ExportCalendarCommandTest {


  @Test
  public void testExportCalendarReturnMessage() throws Exception {
    CalendarModel calendara = new CalendarModel();
    ICalendarService calendar = new CalendarService(calendara);
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    calendar.addSingleEvent("Meeting", start, end, "desc", "room", true, false);

    String fileName = "test_export_message.csv";
    ExportCalendarCommand exportCmd = new ExportCalendarCommand(calendar, "csv", fileName);
    String result = exportCmd.execute();

    assertNotEquals("", result);
    assertTrue(result.startsWith("Calendar exported to:"));

    File exportedFile = new File(fileName);
    assertTrue(exportedFile.exists());
  }


  @Test
  public void testExportCalendarIOException() throws Exception {
    CalendarModel calendara = new CalendarModel();
    ICalendarService calendar = new CalendarService(calendara);
    String invalidFileName = "/nonexistent_folder/test_export.csv";
    ExportCalendarCommand exportCmd = new ExportCalendarCommand(calendar, "csv", invalidFileName);
    String result = exportCmd.execute();
    assertTrue(result.startsWith("Export failed:"));
  }
}