package model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface defining the behavior of an event. Provides methods to access event details and check
 * for conflicts.
 */
public interface Event {

  /**
   * Returns the subject of the event.
   *
   * @return The event’s title or name
   */
  String getSubject();

  /**
   * Returns the start date and time of the event.
   *
   * @return The date and time when the event begins
   */
  LocalDateTime getStartDateTime();

  /**
   * Returns the description of the event.
   *
   * @return A brief summary of the event
   */
  String getDescription();

  /**
   * Returns the location of the event.
   *
   * @return The place where the event occurs
   */
  String getLocation();

  /**
   * Indicates whether the event is public.
   *
   * @return True if the event is public, false if private
   */
  boolean isPublic();

  /**
   * Determines if this event conflicts with another event.
   *
   * @param other The other event to check against
   * @return True if there’s a conflict, false otherwise
   */
  boolean conflictsWith(Event other);

  /**
   * Returns a list of event occurrences. For single events, this contains just the event itself;
   * for recurring events, all instances.
   *
   * @return A list of event occurrences
   */
  List<Event> getOccurrences();

  /**
   * Indicates whether conflicting events should be auto-declined.
   *
   * @return True if auto-decline is enabled, false otherwise
   */
  boolean isAutoDecline();

  /**
   * Returns the effective end date and time of the event.
   *
   * @return The end date-time, or a default if not explicitly set
   */
  LocalDateTime getEffectiveEndDateTime();
}
