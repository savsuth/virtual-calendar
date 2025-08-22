package model;

import java.io.IOException;

/**
 * Defines the contract for exporting calendar data.
 */
public interface Exporter {

  /**
   * Exports the given calendar model to the specified output path.
   *
   * @param model      the calendar model to export
   * @param outputPath the file path where the export should be written
   * @return a message indicating success or failure
   * @throws IOException if an I/O error occurs during export
   */
  String export(ICalendarModel model, String outputPath) throws IOException;
}
