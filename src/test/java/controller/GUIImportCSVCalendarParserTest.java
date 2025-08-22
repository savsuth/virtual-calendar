package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import model.CalendarManager;
import model.ICalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Test Cases for {@link GUIImportCSVCalendarParser}.
 */
public class GUIImportCSVCalendarParserTest {

  private GUIImportCSVCalendarParser parser;


  @Before
  public void setUp() throws Exception {

    ICalendarManager manager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(manager);

    service.createCalendar("Default", "UTC");
    service.useCalendar("Default");
    parser = new GUIImportCSVCalendarParser(service);
  }

  @Test
  public void testNullFileName() throws Exception {
    Command cmd = parser.parse(null);
    String result = cmd.execute();
    assertEquals("Error: File name cannot be empty.", result);
  }

  @Test
  public void testEmptyFileName() throws Exception {
    Command cmd = parser.parse("   ");
    String result = cmd.execute();
    assertEquals("Error: File name cannot be empty.", result);
  }

  @Test
  public void testValidCSVImport() throws Exception {

    File tempFile = File.createTempFile("testImport", ".csv");
    tempFile.deleteOnExit();
    String filePath = tempFile.getAbsolutePath();
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

      writer.write(
          "Subject,Start Date,Start Time,End Date,"
              + "End Time,AllDayEvent,Description,Location,Private");
      writer.newLine();

      writer.write("ImportEvent,2025-04-10,09:00,2025-04-10,10:00,false,Desc,Loc,false");
      writer.newLine();
    }

    Command cmd = parser.parse(filePath);
    String result = cmd.execute();

    assertTrue(result.contains("Imported"));
  }

  @Test
  public void testInvalid2() throws Exception {
    String trailingDotFileName = "data.";
    Command cmdTrailing = parser.parse(trailingDotFileName);
    String resultTrailing;
    try {
      resultTrailing = cmdTrailing.execute();
    } catch (Exception e) {
      resultTrailing = e.getMessage();
    }
    assertTrue(resultTrailing.contains("data.") || resultTrailing.contains("Error"));

  }

  @Test
  public void testInvalid3() throws Exception {
    File tempFile = File.createTempFile("testImportUpper", ".CSV");
    tempFile.deleteOnExit();
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
      writer.write(
          "Subject,Start Date,Start Time,End Date,End Time,"
              + "AllDayEvent,Description,Location,Private");
      writer.newLine();
      writer.write("TestEvent,2025-04-10,09:00,2025-04-10,10:00,false,Desc,Loc,false");
      writer.newLine();
    }
    Command cmdUpper = parser.parse(tempFile.getAbsolutePath());
    String resultUpper = cmdUpper.execute();
    assertTrue(resultUpper.contains("Imported"));
  }


}