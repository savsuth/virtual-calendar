package controller;

import model.IMultiCalendarService;

/**
 * Factory for creating calendar import parsers based on file format.
 */
public class GUIImportFactory {

  /**
   * Returns the appropriate import parser for the given format.
   *
   * @param format  the format of the calendar file
   * @param service the calendar service
   * @return the corresponding {@link IGUIImporterParser}
   * @throws IllegalArgumentException if the format is null, empty, or unsupported
   */
  public static IGUIImporterParser getParser(String format, IMultiCalendarService service) {
    if (format == null || format.trim().isEmpty()) {
      throw new IllegalArgumentException("Import format cannot be empty.");
    }
    if (format.equals("csv")) {
      return new GUIImportCSVCalendarParser(service);
    }
    throw new IllegalArgumentException("Unsupported import format: " + format);
  }

}
