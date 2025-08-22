package model;

/**
 * Factory for creating calendar exporters based on format.
 */
public class ExportFactory {

  /**
   * Returns an exporter for the given format.
   *
   * @param format the export format
   * @return the corresponding {@link Exporter}
   * @throws IllegalArgumentException if format is unsupported
   */
  public Exporter getExport(String format) {
    if (format.equals("csv")) {
      return new CSVExporter();
    }
    throw new IllegalArgumentException("Unsupported format: " + format);
  }
}
