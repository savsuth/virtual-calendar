package controller;

import model.IMultiCalendarService;

/**
 * A controller for handling extended calendar commands.
 */
public class ExtendedCalendarController implements ICalendarController {

  private IHeadCommandParser extendedParser;

  /**
   * Constructs a controller with a multi-calendar service and extended parser.
   *
   * @param multiCalendarService The service for managing multiple calendars
   */
  public ExtendedCalendarController(IMultiCalendarService multiCalendarService) {
    this.extendedParser = new ExtendedCommandParser(multiCalendarService);
  }

  /**
   * Processes a command line input and returns the result. Uses the extended parser to handle the
   * command.
   *
   * @param commandLine The input command string to process
   * @return The result of the command execution, or an error message
   */
  @Override
  public String processCommand(String commandLine) {
    Command command = extendedParser.parse(commandLine);
    if (command == null) {
      return "";
    }
    try {
      return command.execute();
    } catch (Exception e) {
      return "Error processing command: " + e.getMessage();
    }
  }
}