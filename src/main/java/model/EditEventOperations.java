package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles operations for editing events in a calendar model.
 */
public class EditEventOperations implements IEditEventOperations {

  private Map<RecurringEvent, Map<LocalDateTime,
      SingleEvent>> recurringOverrides = new HashMap<>();

  /**
   * Edits an event’s property based on the specified mode.
   *
   * @param model    The calendar model containing the events
   * @param subject  The subject of the event to edit
   * @param from     The start time for editing (null for ALL mode)
   * @param property The property to change (e.g., "subject", "start")
   * @param newValue The new value for the property
   * @param mode     The edit mode (SINGLE, FROM, ALL)
   * @throws Exception If the edit fails or no matching event is found
   */
  @Override
  public void editEvent(ICalendarModel model, String subject, LocalDateTime from, String property,
      String newValue, ICalendarService.EditMode mode) throws Exception {
    boolean edited = false;
    List<Event> events = model.getAllEvents();
    List<Event> newRecurringEvents = new ArrayList<>();

    for (Event event : events) {
      if (!event.getSubject().equalsIgnoreCase(subject)) {
        continue;
      }
      if (!(event instanceof RecurringEvent)) {
        if (mode != ICalendarService.EditMode.SINGLE) {
          throw new UnsupportedOperationException(
              "For non-recurring events, only SINGLE mode is allowed.");
        }
        if (event.getStartDateTime().equals(from)) {
          updateEvent((AbstractEvent) event, property, newValue, model);
          edited = true;
        }
        continue;
      }
      RecurringEvent re = (RecurringEvent) event;
      switch (mode) {
        case ALL:
          updateEvent(re, property, newValue, model);
          edited = true;
          break;
        case SINGLE:
          List<SingleEvent> occs = re.generateOccurrences();
          boolean found = false;
          for (SingleEvent occ : occs) {
            if (occ.getStartDateTime().equals(from)) {
              SingleEvent overrideOcc = createOverride(occ, property, newValue);
              recurringOverrides
                  .computeIfAbsent(re, k -> new HashMap<>())
                  .put(occ.getStartDateTime(), overrideOcc);
              found = true;
              edited = true;
              break;
            }
          }
          if (!found) {
            throw new Exception("No occurrence found at the specified time for recurring event.");
          }
          break;
        case FROM:
          List<SingleEvent> allOccs = re.generateOccurrences();
          List<SingleEvent> futureOccs = new ArrayList<>();
          for (SingleEvent occ : allOccs) {
            if (!occ.getStartDateTime().isBefore(from)) {
              futureOccs.add(occ);
            }
          }
          if (futureOccs.isEmpty()) {
            break;
          }
          LocalDate earliestFutureDay = futureOccs.get(0).getStartDateTime().toLocalDate();
          LocalDate dayBefore = earliestFutureDay.minusDays(1);
          re.setRecurrenceEndDate(dayBefore);
          RecurringEvent newRe = createSplitRecurringEvent(re, futureOccs, property,
              newValue, model);
          newRecurringEvents.add(newRe);
          edited = true;
          break;

        default:
          throw new UnsupportedOperationException();
      }
    }
    for (Event newEvent : newRecurringEvents) {
      model.addEvent(newEvent,
          newEvent instanceof AbstractEvent && newEvent.isAutoDecline());
    }
    if (!edited) {
      throw new Exception("No matching event found to edit.");
    }
  }

  /**
   * Updates an event’s property with a new value.
   *
   * @param event    The event to update
   * @param property The property to change
   * @param newValue The new value
   * @param model    The calendar model for conflict checking
   * @throws Exception If the property is unsupported or the edit causes a conflict
   */
  private void updateEvent(AbstractEvent event, String property, String newValue,
      ICalendarModel model)
      throws Exception {
    if (property.equalsIgnoreCase("subject")) {
      event.setSubject(newValue);
    } else if (property.equalsIgnoreCase("description")) {
      event.setDescription(newValue);
    } else if (property.equalsIgnoreCase("location")) {
      event.setLocation(newValue);
    } else if (property.equalsIgnoreCase("public")) {
      event.setPublic(Boolean.parseBoolean(newValue));
    } else if (property.equalsIgnoreCase("autodecline")) {
      event.setAutoDecline(true);
    } else if (property.equalsIgnoreCase("start")
        || property.equalsIgnoreCase("startdatetime")) {
      LocalDateTime newStart = LocalDateTime.parse(newValue);
      if (event instanceof SingleEvent) {
        SingleEvent se = (SingleEvent) event;
        if (newStart.isAfter(se.getEffectiveEndDateTime())) {
          throw new IllegalArgumentException("Start time cannot be after end time.");
        }
        LocalDateTime oldStart = event.getStartDateTime();
        event.setStartDateTime(newStart);
        if (event.isAutoDecline() && isConflictWithOthers(event, model)) {
          event.setStartDateTime(oldStart);
          throw new IllegalArgumentException("Edit would cause a conflict.");
        }
      } else {
        throw new UnsupportedOperationException(
            "Editing start time for recurring events in ALL mode not allowed; use FROM mode.");
      }
    } else if (property.equalsIgnoreCase("end")
        || property.equalsIgnoreCase("enddatetime")) {
      LocalDateTime newEnd = LocalDateTime.parse(newValue);
      if (newEnd.isBefore(event.getStartDateTime())) {
        throw new IllegalArgumentException("End time cannot be before start time.");
      }
      if (event instanceof SingleEvent) {
        SingleEvent se = (SingleEvent) event;
        LocalDateTime oldEnd = se.getEffectiveEndDateTime();
        se.setEndDateTime(newEnd);
        if (event.isAutoDecline() && isConflictWithOthers(event, model)) {
          se.setEndDateTime(oldEnd);
          throw new IllegalArgumentException("Edit would cause a conflict.");
        }
      } else {
        throw new UnsupportedOperationException(
            "Editing end time for recurring events in ALL mode not allowed; use FROM mode.");
      }
    } else {
      throw new UnsupportedOperationException("Editing property not supported: " + property);
    }
  }

  /**
   * Checks if an updated event conflicts with others in the calendar.
   *
   * @param updatedEvent The event being updated
   * @param model        The calendar model to check against
   * @return True if a conflict exists, false otherwise
   */
  private boolean isConflictWithOthers(AbstractEvent updatedEvent, ICalendarModel model) {
    for (Event other : model.getAllEvents()) {
      if (other == updatedEvent) {
        continue;
      }
      if (updatedEvent.conflictsWith(other) || other.conflictsWith(updatedEvent)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Creates a new recurring event for future occurrences after a split.
   *
   * @param oldRe      The original recurring event
   * @param futureOccs The list of future occurrences
   * @param property   The property to update
   * @param newValue   The new value for the property
   * @param model      The calendar model for context
   * @return A new recurring event with the updated property
   * @throws Exception If the creation or update fails
   */
  private RecurringEvent createSplitRecurringEvent(
      RecurringEvent oldRe, List<SingleEvent> futureOccs,
      String property, String newValue, ICalendarModel model)
      throws Exception {
    LocalDateTime newStart = futureOccs.get(0).getStartDateTime();
    LocalDateTime newEnd = LocalDateTime.of(newStart.toLocalDate(),
        oldRe.getEffectiveEndDateTime().toLocalTime());
    LocalDate lastDay = futureOccs.get(futureOccs.size() - 1).getStartDateTime().toLocalDate();
    RecurringEvent newRe = new RecurringEvent(oldRe.getSubject(),
        newStart, newEnd, oldRe.getDescription(), oldRe.getLocation(), oldRe.isPublic(),
        oldRe.getRecurrenceDays(), oldRe.getOccurrenceCount(), lastDay);
    newRe.setAutoDecline(true);
    updateEvent(newRe, property, newValue, model);
    return newRe;
  }

  /**
   * Creates an override for a single occurrence of a recurring event.
   *
   * @param occ      The original occurrence
   * @param property The property to change
   * @param newValue The new value
   * @return A new single event with the updated property
   * @throws Exception If the property is unsupported or invalid
   */
  private SingleEvent createOverride(SingleEvent occ, String property, String newValue)
      throws Exception {
    SingleEvent overrideOcc = new SingleEvent(occ.getSubject(), occ.getStartDateTime(),
        occ.getEffectiveEndDateTime(), occ.getDescription(),
        occ.getLocation(), occ.isPublic());
    overrideOcc.setAutoDecline(true);
    if (property.equalsIgnoreCase("subject")) {
      overrideOcc.setSubject(newValue);
    } else if (property.equalsIgnoreCase("description")) {
      overrideOcc.setDescription(newValue);
    } else if (property.equalsIgnoreCase("location")) {
      overrideOcc.setLocation(newValue);
    } else if (property.equalsIgnoreCase("public")) {
      overrideOcc.setPublic(Boolean.parseBoolean(newValue));
    } else if (property.equalsIgnoreCase("autodecline")) {
      overrideOcc.setAutoDecline(true);
    } else if (property.equalsIgnoreCase("start")
        || property.equalsIgnoreCase("startdatetime")) {
      LocalDateTime newStart = LocalDateTime.parse(newValue);
      if (newStart.isAfter(occ.getEffectiveEndDateTime())) {
        throw new IllegalArgumentException("Start time cannot be after end time.");
      }
      overrideOcc.setStartDateTime(newStart);
    } else if (property.equalsIgnoreCase("end")
        || property.equalsIgnoreCase("enddatetime")) {
      LocalDateTime newEnd = LocalDateTime.parse(newValue);
      if (newEnd.isBefore(occ.getStartDateTime())) {
        throw new IllegalArgumentException("End time cannot be before start time.");
      }
      overrideOcc.setEndDateTime(newEnd);
    } else {
      throw new UnsupportedOperationException("Editing property not supported: " + property);
    }
    return overrideOcc;
  }
}