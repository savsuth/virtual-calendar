package model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single event occurrence.
 */
public class SingleEvent extends AbstractEvent {


  /**
   * Constructs a SingleEvent with the specified parameters.
   *
   * @param subject       the event subject
   * @param startDateTime the start date and time of the event
   * @param endDateTime   the end date and time of the event; must be after startDateTime
   * @param description   a description of the event
   * @param location      the location of the event
   * @param isPublic      true if the event is public, false otherwise
   * @throws InvalidDateException if endDateTime is before startDateTime
   */
  public SingleEvent(String subject, LocalDateTime startDateTime, LocalDateTime endDateTime,
      String description, String location, boolean isPublic)
      throws InvalidDateException {
    super(subject, startDateTime, description, location, isPublic);
    if (endDateTime != null && endDateTime.isBefore(startDateTime)) {
      throw new InvalidDateException("End date & time must be after start date & time.");
    }
    this.endDateTime = endDateTime;
  }


  /**
   * Checks if this event conflicts with another event.
   *
   * @param other the other event to check against
   * @return true if the events conflict, false otherwise
   */
  @Override
  public boolean conflictsWith(Event other) {
    if (other instanceof SingleEvent) {
      SingleEvent o = (SingleEvent) other;
      LocalDateTime start1 = this.startDateTime;
      LocalDateTime end1 = this.getEffectiveEndDateTime();
      LocalDateTime start2 = o.getStartDateTime();
      LocalDateTime end2 = o.getEffectiveEndDateTime();
      return start1.isBefore(end2) && start2.isBefore(end1);
    } else if (other instanceof RecurringEvent) {
      RecurringEvent rec = (RecurringEvent) other;
      for (SingleEvent occurrence : rec.generateOccurrences()) {
        if (this.conflictsWith(occurrence)) {
          return true;
        }
      }
      return false;
    }
    return false;
  }


  /**
   * Returns a list containing this event as its sole occurrence.
   *
   * @return a singleton list with this event
   */
  @Override
  public List<Event> getOccurrences() {
    return Collections.singletonList(this);
  }
}
