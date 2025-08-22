package model;

/**
 * Exception thrown when an event conflicts with an existing event in the calendar.
 */
public class EventConflictException extends Exception {

  /**
   * Constructs a new EventConflictException with the specified detail message.
   *
   * @param s The detail message explaining the conflict
   */
  public EventConflictException(String s) {
    super(s);
  }
}