package model;

/**
 * Exception thrown when an invalid date format or date logic is encountered.
 */
public class InvalidDateException extends Exception {

  /**
   * Constructs a new InvalidDateException with the specified detail message.
   *
   * @param s the detail message
   */
  public InvalidDateException(String s) {
    super(s);
  }
}
