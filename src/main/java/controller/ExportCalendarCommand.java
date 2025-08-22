package controller;

import java.io.IOException;
import model.ICalendarService;

/**
 * Exports the calendar data to a CSV file and implements the Command pattern for exporting calendar
 * contents.
 */
public class ExportCalendarCommand implements Command {

  private ICalendarService calendarService;
  private String format;
  private String fileName;

  /**
   * Constructs a command to export calendar data to a CSV file.
   *
   * @param calendarService The service managing the calendar
   * @param fileName        The name of the file to export to
   */
  public ExportCalendarCommand(ICalendarService calendarService, String format, String fileName) {
    this.calendarService = calendarService;
    this.format = format;
    this.fileName = fileName;
  }

  /**
   * Executes the export operation.
   *
   * @return A message indicating the CSV export path or an error if it fails
   * @throws Exception if an error occurs during the export process
   */
  @Override
  public String execute() throws Exception {
    try {
      String path = calendarService.exportTo(format, fileName);
      return "Calendar exported to: " + path;
    } catch (IOException e) {
      return "Export failed: " + e.getMessage();
    }
  }
}