package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface for the calendar model that stores and retrieves events.
 */
public interface ICalendarModel {

  /**
   * Adds an event to the model.
   *
   * @param event       the event to add
   * @param autoDecline whether conflicting events should be auto-declined
   * @throws EventConflictException if the event conflicts with an existing one
   */
  void addEvent(Event event, boolean autoDecline) throws EventConflictException;

  /**
   * Returns all events scheduled on the given date.
   *
   * @param date the date to search
   * @return list of events
   */
  List<Event> getEventsOn(LocalDate date);

  /**
   * Returns all events in the calendar.
   *
   * @return list of all events
   */
  List<Event> getAllEvents();

  /**
   * Checks whether the calendar is busy at a specific date and time.
   *
   * @param dateTime the date-time to check
   * @return true if there is a conflict, false otherwise
   */
  boolean isBusyAt(LocalDateTime dateTime);
}

