package controller;

/**
 * Interface for command parsers that convert tokenized input into a Command.
 */
public interface ICommandParser {

  /**
   * Parses the given tokens and returns the corresponding Command.
   *
   * @param tokens the array of command tokens
   * @return a Command instance based on the input tokens
   */
  Command parse(String[] tokens);
}

