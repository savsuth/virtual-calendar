package controller;

import java.time.LocalDate;
import model.IMultiCalendarService;

/**
 * A command class for copying multiple events between date ranges across calendars. Implements the
 * Command pattern to handle bulk event copying operations.
 */
public class CopyEventsBetweenCommand implements Command {

  private IMultiCalendarService multiCalendarService;
  private LocalDate sourceStartDate;
  private LocalDate sourceEndDate;
  private String targetCalendarName;
  private LocalDate targetStartDate;

  /**
   * Constructor for the class.
   *
   * @param multiCalendarService The service managing multiple calendars
   * @param sourceStartDate      The start date of the source event range
   * @param sourceEndDate        The end date of the source event range
   * @param targetCalendarName   The name of the calendar to copy events to
   * @param targetStartDate      The start date in the target calendar
   */
  public CopyEventsBetweenCommand(IMultiCalendarService multiCalendarService,
      LocalDate sourceStartDate,
      LocalDate sourceEndDate, String targetCalendarName,
      LocalDate targetStartDate) {
    this.multiCalendarService = multiCalendarService;
    this.sourceStartDate = sourceStartDate;
    this.sourceEndDate = sourceEndDate;
    this.targetCalendarName = targetCalendarName;
    this.targetStartDate = targetStartDate;
  }

  /**
   * Executes the bulk event copy operation using the calendar service.
   *
   * @return A string result from the copy operation
   * @throws Exception if the copy operation encounters an error
   */
  @Override
  public String execute() throws Exception {
    return multiCalendarService.copyEventsBetween(sourceStartDate, sourceEndDate,
        targetCalendarName, targetStartDate);
  }
}
