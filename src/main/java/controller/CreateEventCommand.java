package controller;

import java.time.LocalDateTime;
import model.ICalendarService;

/**
 * Creates a single event in the calendar and implements the Command pattern.
 */
public class CreateEventCommand implements Command {

  private ICalendarService service;
  private boolean autoDecline;
  private String subject;
  private LocalDateTime start;
  private LocalDateTime end;
  private String description;
  private String location;
  private boolean isPublic;

  /**
   * Constructor of the class.
   *
   * @param service     The calendar service to manage the event
   * @param autoDecline Whether to automatically decline conflicting events
   * @param subject     The title or name of the event
   * @param start       The start date and time of the event
   * @param end         The end date and time of the event
   * @param description A brief description of the event
   * @param location    The location where the event will take place
   * @param isPublic    Whether the event is visible to others
   */
  public CreateEventCommand(ICalendarService service, boolean autoDecline, String subject,
      LocalDateTime start, LocalDateTime end, String description,
      String location, boolean isPublic) {
    this.service = service;
    this.autoDecline = autoDecline;
    this.subject = subject;
    this.start = start;
    this.end = end;
    this.description = description;
    this.location = location;
    this.isPublic = isPublic;
  }

  /**
   * Executes the event creation by adding a new single event to the calendar.
   *
   * @return A message indicating the event was created successfully
   * @throws Exception if an error occurs during event creation
   */
  @Override
  public String execute() throws Exception {
    service.addSingleEvent(subject, start, end, description, location, isPublic, autoDecline);
    return "Event created: " + subject;
  }
}
