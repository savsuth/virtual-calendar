package model;

/**
 * Factory for creating calendar data importers based on format.
 */
public class ImportFactory {

  /**
   * Returns an importer for the specified format.
   *
   * @param format the import format
   * @return the corresponding importer
   * @throws IllegalArgumentException if format is unsupported
   */
  public Importer getImporter(String format) {
    if (format.equals("csv")) {
      return new CSVImporter();
    }
    throw new IllegalArgumentException("Unsupported format: " + format);
  }
}
