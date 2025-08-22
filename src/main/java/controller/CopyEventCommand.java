package controller;

import java.time.LocalDateTime;
import model.IMultiCalendarService;

/**
 * A command class that handles copying an event from one calendar to another. Implements the
 * Command pattern to encapsulate the copy operation.
 */
public class CopyEventCommand implements Command {

  private IMultiCalendarService multiCalendarService;
  private String eventName;
  private LocalDateTime sourceStart;
  private String targetCalendarName;
  private LocalDateTime targetStart;

  /**
   * Creates a new CopyEventCommand with all required parameters for copying an event.
   *
   * @param multiCalendarService The service handling multiple calendars
   * @param eventName            The name of the event to be copied
   * @param sourceStart          The start time of the event in the source calendar
   * @param targetCalendarName   The name of the destination calendar
   * @param targetStart          The desired start time in the target calendar
   */
  public CopyEventCommand(IMultiCalendarService multiCalendarService, String eventName,
      LocalDateTime sourceStart,
      String targetCalendarName, LocalDateTime targetStart) {
    this.multiCalendarService = multiCalendarService;
    this.eventName = eventName;
    this.sourceStart = sourceStart;
    this.targetCalendarName = targetCalendarName;
    this.targetStart = targetStart;
  }

  /**
   * Executes the copy event operation using the calendar service.
   *
   * @return A string result from the copy operation
   * @throws Exception if the copy operation fails
   */
  @Override
  public String execute() throws Exception {
    return multiCalendarService.copyEvent(eventName, sourceStart, targetCalendarName,
        targetStart);
  }
}