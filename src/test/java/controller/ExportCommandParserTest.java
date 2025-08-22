package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import model.CalendarModel;
import model.CalendarService;
import model.ICalendarService;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Test Case for {@link ExportCommandParserTest}.
 */

public class ExportCommandParserTest {

  private ExportCommandParser parser;

  /**
   * Sets up the test fixture before each test.
   */
  @Before
  public void setUp() {
    CalendarModel calendara = new CalendarModel();
    ICalendarService calendar = new CalendarService(calendara);
    parser = new ExportCommandParser(calendar);
  }


  @Test
  public void testValidExportCommand() throws Exception {
    String[] tokens = "export cal test.csv".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    String result = cmd.execute();
    assertTrue(result.startsWith("Calendar exported to:"));
  }


  @Test
  public void testInvalidExportCommand() throws Exception {
    String[] tokens = "export cal".split("\\s+");
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    String result = cmd.execute();
    assertEquals("Invalid export command.", result);
  }


  @Test
  public void testExportParserException() throws Exception {
    String[] tokens = new String[]{"export", null, "test.csv"};
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    String result = cmd.execute();
    assertTrue(result.startsWith("Error processing export command:"));
  }


  @Test
  public void testValidExport2() throws Exception {
    String[] tokens = {"export", "cal", "test.csv"};
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    String result = cmd.execute();
    assertTrue(result.startsWith("Calendar exported to:"));
  }


  @Test
  public void testValidExport3() throws Exception {
    String[] tokens = {"export", "CAL", "test.csv", "extraToken"};
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    String result = cmd.execute();
    assertTrue(result.startsWith("Calendar exported to:"));
  }


  @Test
  public void testInvalidExportCommandWrongKeyword() throws Exception {
    String[] tokens = {"export", "file", "test.csv"};
    Command cmd = parser.parse(tokens);
    assertNotNull(cmd);
    String result = cmd.execute();
    assertEquals("Invalid export command.", result);
  }
}