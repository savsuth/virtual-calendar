package controller;

import java.time.LocalDateTime;
import model.ICalendarService;

/**
 * Command to show the availability status of the calendar at a given time.
 */
public class ShowStatusCommand implements Command {

  private ICalendarService calendar;
  private LocalDateTime dateTime;

  /**
   * Constructs a command to check availability at a specific date-time.
   *
   * @param calendar The calendar service to check
   * @param dateTime The date and time to check status for
   */
  public ShowStatusCommand(ICalendarService calendar, LocalDateTime dateTime) {
    this.calendar = calendar;
    this.dateTime = dateTime;
  }

  /**
   * Executes the command by checking if any event overlaps the specified date-time.
   *
   * @return "Busy" if an event is found at the given time, "Available" otherwise
   * @throws Exception if an error occurs during execution
   */
  public String execute() {
    boolean busy = calendar.isBusyAt(dateTime);
    return busy ? "Busy" : "Available";
  }
}