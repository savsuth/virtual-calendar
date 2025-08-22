package controller;

/**
 * A simple interface for different export parsers. Each parser can create the appropriate Command
 * for exporting in a given format.
 */
public interface IGUIExporterParser {

  Command parse(String filename);

}
