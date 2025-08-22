package controller;

/**
 * Interface for top-level command parsers that process raw command lines.
 */
public interface IHeadCommandParser {

  /**
   * Parses a command line string into a Command object.
   *
   * @param commandLine The raw command string to parse
   * @return A Command instance corresponding to the input
   */
  Command parse(String commandLine);
}