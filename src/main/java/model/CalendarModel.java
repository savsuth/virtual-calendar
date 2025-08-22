package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the calendar model that stores events and provides methods to add and query events.
 */
public class CalendarModel implements ICalendarModel {

  private List<Event> events;

  /**
   * Constructs a new CalendarModel with an empty list of events.
   */
  public CalendarModel() {
    this.events = new ArrayList<>();
  }

  /**
   * Adds an event to the calendar, checking for conflicts if auto-decline is enabled.
   *
   * @param event       The event to add
   * @param autoDecline Whether to check for and reject conflicts
   * @throws EventConflictException If a conflict is detected and auto-decline is true
   */
  @Override
  public void addEvent(Event event, boolean autoDecline) throws EventConflictException {
    for (Event existingEvent : events) {
      if (existingEvent.conflictsWith(event)) {
        if (event.isAutoDecline()) {
          throw new EventConflictException("Event '" + event.getSubject() +
              "' conflicts with existing event '" + existingEvent.getSubject() + "'.");
        }
      }
    }
    events.add(event);
  }

  /**
   * Gets all events occurring on a specific date.
   *
   * @param date The date to check for events
   * @return A list of events on the given date
   */
  @Override
  public List<Event> getEventsOn(LocalDate date) {
    List<Event> result = new ArrayList<>();
    for (Event event : events) {
      if (occursOnDate(event, date)) {
        result.add(event);
      }
    }
    return result;
  }

  /**
   * Checks if an event occurs on a given date.
   *
   * @param event The event to check
   * @param date  The date to verify
   * @return True if the event occurs on the specified date, false otherwise
   */
  private boolean occursOnDate(Event event, LocalDate date) {
    if (event instanceof SingleEvent) {
      LocalDate start = event.getStartDateTime().toLocalDate();
      LocalDate end = event.getEffectiveEndDateTime().toLocalDate();
      return (!date.isBefore(start)) && (!date.isAfter(end));
    } else {
      for (Event occurrence : event.getOccurrences()) {
        if (occurrence.getStartDateTime().toLocalDate().equals(date)) {
          return true;
        }
      }
      return false;
    }
  }

  /**
   * Returns all events in the calendar.
   *
   * @return A list of all events
   */
  @Override
  public List<Event> getAllEvents() {
    return new ArrayList<>(events);
  }

  /**
   * Checks if the calendar is busy at a specific date-time.
   *
   * @param dateTime The date-time to check
   * @return True if an event overlaps the given time, false otherwise
   */
  @Override
  public boolean isBusyAt(LocalDateTime dateTime) {
    for (Event event : events) {
      for (Event occurrence : event.getOccurrences()) {
        if (occurrence instanceof SingleEvent) {
          Event se = occurrence;
          if (se.getStartDateTime().isBefore(dateTime) && se.getEffectiveEndDateTime()
              .isAfter(dateTime)) {
            return true;
          }
        }
      }
    }
    return false;
  }
}