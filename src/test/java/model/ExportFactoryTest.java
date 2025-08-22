package model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit Test cases for the {@link ExportFactoryTest} class, returns the appropriate Exporter based
 * on the format given. In the test if format is not supported yet, it verifies that.
 */
public class ExportFactoryTest {

  @Test(expected = Exception.class)
  public void getExport() {
    ExportFactory factory = new ExportFactory();
    Exporter exporter = factory.getExport("csv");
    assertNotNull(exporter);
    assertTrue(exporter instanceof CSVExporter);
    factory.getExport("json");

  }


}