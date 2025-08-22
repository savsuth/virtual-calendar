package controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import model.CalendarManager;
import model.ICalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.Before;
import org.junit.Test;

/**
 * JUNIT Test case for {@link GUIImportFactory}, which is a factory of different format of Import
 * Parsers.
 */
public class GUIImportFactoryTest {

  private IMultiCalendarService service;

  @Before
  public void setUp() {

    ICalendarManager manager = new CalendarManager();
    service = new MultiCalendarService(manager);
  }

  @Test
  public void testGetParserCsv() {

    Object parser = GUIImportFactory.getParser("csv", service);
    assertNotNull("Parser should not be null for 'csv' format", parser);
    assertTrue("Parser should be an instance of GUIImportCSVCalendarParser",
        parser instanceof GUIImportCSVCalendarParser);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyFormatThrowsException() {

    GUIImportFactory.getParser("", service);
  }

  @Test
  public void testUnsupportedFormatThrowsException() {
    try {
      GUIImportFactory.getParser("xml", service);
      fail("Expected IllegalArgumentException for unsupported import format.");
    } catch (IllegalArgumentException ex) {
      assertTrue(ex.getMessage().contains("Unsupported import format"));
    }
  }

}