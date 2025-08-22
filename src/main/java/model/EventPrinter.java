package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Prints calendar events for a specific date or date-time range using a given calendar model.
 */
public class EventPrinter implements IEventPrinter {

  private ICalendarModel model;

  /**
   * Constructs an EventPrinter with the specified calendar model.
   *
   * @param model the calendar model used to retrieve events
   */
  public EventPrinter(ICalendarModel model) {
    this.model = model;
  }

  /**
   * Prints all events scheduled on the given date.
   *
   * @param date the date for which to print events
   * @return a string representation of events on the specified date
   * @throws Exception if an error occurs while retrieving events
   */
  @Override
  public String printEventsOn(LocalDate date) throws Exception {
    List<Event> events = model.getEventsOn(date);

    if (events.isEmpty()) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    sb.append("Events on ").append(date).append(":\n");
    for (Event event : events) {
      sb.append("- ").append(event.getSubject()).append(" at ");
      String location = event.getLocation();
      if (location != null && !location.trim().isEmpty()) {
        sb.append(location).append(" ");
      }
      sb.append(event.getStartDateTime()).append("\n");
    }
    return sb.toString();
  }

  /**
   * Prints all events that occur between the given start and end timestamps.
   *
   * @param start the start of the time range
   * @param end   the end of the time range
   * @return a string listing all events in the specified range
   * @throws Exception if an error occurs while retrieving events
   */
  @Override
  public String printEventsRange(LocalDateTime start, LocalDateTime end) throws Exception {
    StringBuilder sb = new StringBuilder();
    sb.append("Events from ").append(start).append(" to ").append(end).append(":\n");
    List<Event> events = model.getAllEvents();
    for (Event event : events) {
      for (Event occurrence : event.getOccurrences()) {
        LocalDateTime occStart = occurrence.getStartDateTime();
        if ((occStart.equals(start) || occStart.isAfter(start)) && occStart.isBefore(end)) {
          sb.append("- ").append(occurrence.getSubject()).append(" at ");
          String location = occurrence.getLocation();
          if (location != null && !location.trim().isEmpty()) {
            sb.append(location).append(" ");
          }
          sb.append(occStart).append("\n");
        }
      }
    }
    return sb.toString();
  }
}
