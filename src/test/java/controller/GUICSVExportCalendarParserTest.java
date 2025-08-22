package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import model.CalendarManager;
import model.ICalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUNIT Test Class for {@link GUICSVExportCalendarParser} which parses CSV formats.
 */
public class GUICSVExportCalendarParserTest {

  private GUICSVExportCalendarParser parser;
  private String exportFileName;


  @Before
  public void setUp() throws Exception {

    ICalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);

    service.createCalendar("Default", "UTC");
    service.useCalendar("Default");

    parser = new GUICSVExportCalendarParser(service);
    exportFileName = "testExport.csv";

    File file = new File(Paths.get(System.getProperty("user.dir"), exportFileName).toString());
    if (file.exists()) {
      file.delete();
    }
  }

  @After
  public void tearDown() throws Exception {

    File file = new File(Paths.get(System.getProperty("user.dir"), exportFileName).toString());
    if (file.exists()) {
      file.delete();
    }
  }

  @Test
  public void testExportWithCsvExtension() {

    Command command = parser.parse(exportFileName);
    String result = "";
    try {
      result = command.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }

    assertTrue(result.startsWith("Calendar exported to:"));

    File file = new File(Paths.get(System.getProperty("user.dir"), exportFileName).toString());
    assertTrue(file.exists());

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String header = br.readLine();
      String expectedHeader = "Subject,Start Date,Start Time,End Date,End Time,"
          + "AllDayEvent,Description,Location,Private";
      assertEquals(expectedHeader, header);
    } catch (Exception e) {
      fail("Failed to read exported file: " + e.getMessage());
    }
  }

  @Test
  public void testExportWithoutExtension() {

    String fileNameNoExt = "testExportNoExt";

    Command command = parser.parse(fileNameNoExt);
    String result = "";
    try {
      result = command.execute();
    } catch (Exception e) {
      fail("Execution threw an exception: " + e.getMessage());
    }
    assertTrue(result.startsWith("Calendar exported to:"));

    File file = new File(Paths.get(System.getProperty("user.dir"), fileNameNoExt).toString());
    assertTrue(file.exists());

    file.delete();
  }
}