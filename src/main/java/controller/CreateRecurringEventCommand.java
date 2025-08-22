package controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import model.ICalendarService;

/**
 * Creates a recurring event in the calendar.
 */
public class CreateRecurringEventCommand implements Command {

  private ICalendarService calendarService;
  private boolean autoDecline;
  private String subject;
  private LocalDateTime start;
  private LocalDateTime end;
  private String description;
  private String location;
  private boolean isPublic;
  private Set<DayOfWeek> recurrenceDays;
  private int occurrenceCount = -1;
  private LocalDate recurrenceEndDate;

  /**
   * Constructor of the class.
   *
   * @param service         The calendar service to manage the event
   * @param autoDecline     Whether to automatically decline conflicting events
   * @param subject         The title or name of the event
   * @param start           The start date and time of the first occurrence
   * @param end             The end date and time of each occurrence
   * @param description     A brief description of the event
   * @param location        The location where the event will take place
   * @param isPublic        Whether the event is visible to others
   * @param recurrenceDays  The days of the week when the event repeats
   * @param occurrenceCount The number of times the event should occur
   */
  public CreateRecurringEventCommand(ICalendarService service, boolean autoDecline, String subject,
      LocalDateTime start, LocalDateTime end, String description,
      String location, boolean isPublic,
      Set<DayOfWeek> recurrenceDays, int occurrenceCount,
      LocalDate recurrenceEndDate) {
    this.calendarService = service;
    this.autoDecline = autoDecline;
    this.subject = subject;
    this.start = start;
    this.end = end;
    this.description = description;
    this.location = location;
    this.isPublic = isPublic;
    this.recurrenceDays = recurrenceDays;
    this.occurrenceCount = occurrenceCount;
    this.recurrenceEndDate = recurrenceEndDate;
  }

  /**
   * Executes the recurring event creation by adding it to the calendar.
   *
   * @return A message confirming the recurring event was created
   * @throws Exception if an error occurs during event creation
   */
  @Override
  public String execute() throws Exception {
    calendarService.addRecurringEvent(subject, start, end, description, location, isPublic,
        recurrenceDays, occurrenceCount, recurrenceEndDate, autoDecline);
    return "Recurring event created: " + subject;
  }
}