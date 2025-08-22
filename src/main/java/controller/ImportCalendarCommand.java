package controller;

import model.IMultiCalendarService;

/**
 * Represents a command to import a calendar from a file in the specified format.
 */

public class ImportCalendarCommand implements Command {

  private IMultiCalendarService service;
  private String format;
  private String fileName;

  /**
   * Constructs the command with service, format, and file name.
   *
   * @param service   the calendar service
   * @param format    import format
   * @param fileName  input file name
   */
  public ImportCalendarCommand(IMultiCalendarService service, String format, String fileName) {
    this.service = service;
    this.format = format;
    this.fileName = fileName;
  }

  @Override
  public String execute() throws Exception {
    try {
      return service.importFrom(format, fileName);
    } catch (Exception e) {
      return "Import failed: " + e.getMessage();
    }
  }
}
