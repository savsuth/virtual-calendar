package controller;

import model.IMultiCalendarService;

/**
 * Parses input for exporting a calendar in CSV format.
 */
public class GUICSVExportCalendarParser implements IGUIExporterParser {

  IMultiCalendarService service;

  /**
   * Constructs a parser for CSV calendar export.
   *
   * @param service the calendar service
   */
  public GUICSVExportCalendarParser(IMultiCalendarService service) {
    this.service = service;
  }

  @Override
  public Command parse(String fileName) {

    try {
      String format = "csv";  // Default format
      int dotIndex = fileName.lastIndexOf(".");
      if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
        format = fileName.substring(dotIndex + 1).toLowerCase();
      }
      return new ExportCalendarCommand(service, format, fileName);
    } catch (Exception e) {
      return () -> "Error processing " + e.getMessage();
    }

  }
}
