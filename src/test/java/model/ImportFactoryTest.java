package model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit Test cases for the {@link ImportFactory} class, returns the appropriate Importer based on
 * the format given. In the test if format is not supported yet, it verifies that.
 */
public class ImportFactoryTest {

  @Test(expected = Exception.class)
  public void getImporter() {
    ImportFactory factory = new ImportFactory();
    Importer importer = factory.getImporter("csv");
    assertNotNull(importer);
    assertTrue(importer instanceof CSVImporter);

    factory.getImporter("json");
  }

}