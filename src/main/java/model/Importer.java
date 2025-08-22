package model;

import java.io.IOException;

/**
 * Represents an import operation interface. Can be implemented for different format imports.
 */
public interface Importer {

  /**
   * Imports event data into the given calendar model from a file.
   *
   * @param model    the calendar model to populate
   * @param fileName the source file path
   * @return status message of the import
   * @throws IOException if file reading fails
   */
  String importData(ICalendarModel model, String fileName) throws IOException;
}
