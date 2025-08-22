package controller;

/**
 * Represents a command interface that can be executed. This is part of Command Design Pattern.
 */
public interface Command {

  /**
   * Executes the command.
   *
   * @return the result of the command execution
   * @throws Exception if an error occurs during execution
   */
  String execute() throws Exception;
}
