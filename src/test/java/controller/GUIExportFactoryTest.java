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
 * Test for the GUIExportFactory, which is a factory of Export Parsers, to see if the correct Parser
 * is returned.
 */
public class GUIExportFactoryTest {

  private IMultiCalendarService service;

  @Before
  public void setUp() {
    ICalendarManager manager = new CalendarManager();
    service = new MultiCalendarService(manager);
  }

  @Test
  public void testGetParserCsv() {

    IGUIExporterParser parser = GUIExportFactory.getParser("csv", service);
    assertNotNull(parser);
    assertTrue(parser instanceof GUICSVExportCalendarParser);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetParserEmptyFormat() {

    GUIExportFactory.getParser("", service);
  }

  @Test
  public void testGetParserUnsupportedFormat() {

    try {
      GUIExportFactory.getParser("json", service);
      fail("Expected IllegalArgumentException for unsupported format.");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Unsupported export format"));
    }
  }

}