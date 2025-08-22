package controller;

import model.IMultiCalendarService;

/**
 * Parser for importing calendar data from a CSV file.
 */
public class GUIImportCSVCalendarParser implements IGUIImporterParser {

  private IMultiCalendarService service;

  /**
   * Constructs the CSV import parser with the given calendar service.
   *
   * @param service the calendar service
   */
  public GUIImportCSVCalendarParser(IMultiCalendarService service) {
    this.service = service;
  }

  @Override
  public Command parse(String fileName) {
    if (fileName == null || fileName.trim().isEmpty()) {
      return () -> "Error: File name cannot be empty.";
    }
    fileName = fileName.trim();
    String format = "csv"; // default
    int dotIndex = fileName.lastIndexOf(".");
    if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
      format = fileName.substring(dotIndex + 1).toLowerCase().trim();

    }
    return new ImportCalendarCommand(service, format, fileName);
  }
}
