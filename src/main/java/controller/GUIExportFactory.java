package controller;

import model.IMultiCalendarService;

/**
 * Factory for creating exporter parsers based on format.
 */
public class GUIExportFactory {


  /**
   * Returns the appropriate exporter parser for the given format.
   *
   * @param format  export format
   * @param service the calendar service
   * @return the exporter parser
   * @throws IllegalArgumentException if format is empty or unsupported
   */
  public static IGUIExporterParser getParser(String format, IMultiCalendarService service) {
    if (format == null || format.trim().isEmpty()) {
      throw new IllegalArgumentException("Export format cannot be empty.");
    }
    if (format.toLowerCase().equals("csv")) {
      return new GUICSVExportCalendarParser(service);
    }
    throw new IllegalArgumentException("Unsupported export format: " + format);
  }
}
