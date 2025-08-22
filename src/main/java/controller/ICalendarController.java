package controller;

/**
 * Interface for a controller that processes calendar-related commands.
 */
public interface ICalendarController {

  /**
   * Processes a command line input and returns the result.
   *
   * @param commandLine The command string to process
   * @return The result of the command execution
   */
  String processCommand(String commandLine);
}
