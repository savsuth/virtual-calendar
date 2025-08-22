package controller;

/**
 * A simple interface for different import parsers. Each parser can create the appropriate Command
 * for importing in a given format.
 */
public interface IGUIImporterParser {

  Command parse(String fileName);

}
