package model;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * A utility class to help copy events (single or recurring) between calendar contexts.
 */
public class EventCopyHelper {

  /**
   * Copies a single event from one calendar to another, using the provided start time in the target
   * calendar.
   *
   * @param sourceCal   The source calendar context.
   * @param targetCal   The target calendar context.
   * @param eventName   The name of the event to be copied.
   * @param sourceStart The original start time of the event in the source calendar.
   * @param targetStart The desired start time in the target calendar.
   * @return A confirmation message describing the copy action.
   * @throws Exception If the event is not found or not a single event.
   */
  public static String copySingleEvent(ICalendarContext sourceCal, ICalendarContext targetCal,
      String eventName, LocalDateTime sourceStart,
      LocalDateTime targetStart) throws Exception {
    SingleEvent sourceEvent = getSingleEvent(sourceCal, eventName, sourceStart);
    long durationMillis = Duration.between(sourceEvent.getStartDateTime(),
        sourceEvent.getEffectiveEndDateTime()).toMillis();
    LocalDateTime targetEnd = targetStart.plus(Duration.ofMillis(durationMillis));
    targetCal.getCalendarService().addSingleEvent(
        sourceEvent.getSubject(),
        targetStart,
        targetEnd,
        sourceEvent.getDescription(),
        sourceEvent.getLocation(),
        sourceEvent.isPublic(),
        true
    );
    return "Event '" + sourceEvent.getSubject() + "' copied to calendar '" + targetCal.getName()
        + "' starting at " + targetStart;
  }

  private static SingleEvent getSingleEvent(ICalendarContext sourceCal, String eventName,
      LocalDateTime sourceStart) throws Exception {
    Event found = null;
    for (Event e : sourceCal.getCalendarService().getAllEvents()) {
      for (Event occ : e.getOccurrences()) {
        if (occ.getSubject().equalsIgnoreCase(eventName) &&
            occ.getStartDateTime().equals(sourceStart)) {
          found = occ;
          break;
        }
      }
      if (found != null) {
        break;
      }
    }
    if (found == null) {
      throw new Exception("Event '" + eventName + "' not found at " + sourceStart);
    }
    if (!(found instanceof SingleEvent)) {
      throw new Exception("Event '" + eventName + "' is not a single event.");
    }
    return (SingleEvent) found;
  }

  /**
   * Copies a recurring event series from one calendar to another.
   *
   * @param sourceCal             The source calendar context.
   * @param targetCal             The target calendar context.
   * @param eventName             The name of the recurring event.
   * @param sourceOccurrenceStart The start time of a known occurrence in the source calendar.
   * @param targetStart           The desired start time for the corresponding occurrence in the
   *                              target calendar.
   * @return A message confirming the recurring event series was copied.
   * @throws Exception If the event is not found or not a recurring event.
   */
  public static String copyRecurringEvent(ICalendarContext sourceCal, ICalendarContext targetCal,
      String eventName, LocalDateTime sourceOccurrenceStart,
      LocalDateTime targetStart) throws Exception {
    RecurringEvent sourceRecurring = getRecurringEvent(sourceCal, eventName,
        sourceOccurrenceStart);
    ZonedDateTime sourceSeriesZDT =
        sourceRecurring.getStartDateTime().atZone(sourceCal.getTimezone());
    Instant seriesInstant = sourceSeriesZDT.toInstant();
    LocalDateTime convertedSeriesStart =
        seriesInstant.atZone(targetCal.getTimezone()).toLocalDateTime();
    Duration offset = Duration.between(convertedSeriesStart, targetStart);
    LocalDateTime newSeriesStart =
        sourceRecurring.getStartDateTime().plus(offset);
    long durationMillis =
        Duration.between(sourceRecurring.getStartDateTime(),
            sourceRecurring.getEffectiveEndDateTime()).toMillis();
    LocalDateTime newSeriesEnd = newSeriesStart.plus(Duration.ofMillis(durationMillis));
    LocalDate newRecurrenceEndDate = null;
    if (sourceRecurring.getRecurrenceEndDate() != null) {
      ZonedDateTime sourceRecurrenceZDT = sourceRecurring.getRecurrenceEndDate().
          atStartOfDay().atZone(sourceCal.getTimezone());
      newRecurrenceEndDate = sourceRecurrenceZDT.toInstant().atZone(targetCal.
          getTimezone()).toLocalDate();
      newRecurrenceEndDate = newRecurrenceEndDate.plusDays(offset.toDays());
    }
    targetCal.getCalendarService().addRecurringEvent(
        sourceRecurring.getSubject(),
        newSeriesStart,
        newSeriesEnd,
        sourceRecurring.getDescription(),
        sourceRecurring.getLocation(),
        sourceRecurring.isPublic(),
        sourceRecurring.getRecurrenceDays(),
        sourceRecurring.getOccurrenceCount(),
        newRecurrenceEndDate,
        true
    );
    return "Recurring event '" + sourceRecurring.getSubject() + "' copied to calendar '" +
        targetCal.getName() + "' starting at " + newSeriesStart;
  }

  private static RecurringEvent getRecurringEvent(ICalendarContext sourceCal, String eventName,
      LocalDateTime sourceOccurrenceStart)
      throws Exception {
    RecurringEvent foundRecurring = null;
    for (Event e : sourceCal.getCalendarService().getAllEvents()) {
      if (e instanceof RecurringEvent && e.getSubject().equalsIgnoreCase(eventName)) {
        RecurringEvent re = (RecurringEvent) e;
        for (Event occ : re.getOccurrences()) {
          if (occ.getStartDateTime().equals(sourceOccurrenceStart)) {
            foundRecurring = re;
            break;
          }
        }
      }
      if (foundRecurring != null) {
        break;
      }
    }
    if (foundRecurring == null) {
      throw new Exception("Recurring event '" + eventName + "' not found at "
          + sourceOccurrenceStart);
    }
    return foundRecurring;
  }

  /**
   * Copies all events from a specific source date to a target date.
   *
   * @param sourceCal  The source calendar context.
   * @param targetCal  The target calendar context.
   * @param sourceDate The date from which events should be copied.
   * @param targetDate The date to which the events should be copied.
   * @return The output, whether it threw error or events got copied.
   * @throws Exception If copying fails for any reason.
   */
  public static String copyEventsOnDate(ICalendarContext sourceCal, ICalendarContext targetCal,
      LocalDate sourceDate, LocalDate targetDate)
      throws Exception {
    List<Event> events = sourceCal.getCalendarService().getEventsOn(sourceDate);
    if (events.isEmpty()) {
      return "No events found on " + sourceDate;
    }
    StringBuilder result = new StringBuilder();
    for (Event e : events) {
      for (Event occ : e.getOccurrences()) {
        if (!occ.getStartDateTime().toLocalDate().equals(sourceDate)) {
          continue;
        }
        LocalTime timeOfDay = occ.getStartDateTime().toLocalTime();
        LocalDateTime targetStart = LocalDateTime.of(targetDate, timeOfDay);
        try {
          if (occ instanceof SingleEvent) {
            String msg = copySingleEvent(sourceCal, targetCal, occ.getSubject(),
                occ.getStartDateTime(), targetStart);
            result.append(msg).append("\n");
          } else if (occ instanceof RecurringEvent) {
            String msg = copyRecurringEvent(sourceCal, targetCal, occ.getSubject(),
                occ.getStartDateTime(), targetStart);
            result.append(msg).append("\n");
          }
        } catch (EventConflictException ex) {
          result.append("Conflict for event '").append(occ.getSubject()).append("'\n");
        }
      }
    }
    return result.toString();
  }

  /**
   * Copies all events from a source date range into a target calendar, starting from the given
   * target date.
   *
   * @param sourceCal       The source calendar context.
   * @param targetCal       The target calendar context.
   * @param sourceStartDate The start date of the source range.
   * @param sourceEndDate   The end date of the source range.
   * @param targetStartDate The start date in the target calendar to align with the source start.
   * @return The output, whether it threw error or events got copied.
   * @throws Exception If an error occurs during copying.
   */
  public static String copyEventsBetweenDates(ICalendarContext sourceCal, ICalendarContext
          targetCal,
      LocalDate sourceStartDate, LocalDate sourceEndDate,
      LocalDate targetStartDate) throws Exception {
    List<Event> events = sourceCal.getCalendarService().getAllEvents();
    if (events.isEmpty()) {
      return "No events found in source calendar.";
    }
    long dayOffset = Duration.between(sourceStartDate.atStartOfDay(),
        targetStartDate.atStartOfDay()).toDays();
    StringBuilder result = new StringBuilder();
    for (Event e : events) {
      for (Event occ : e.getOccurrences()) {
        LocalDate occDate = occ.getStartDateTime().toLocalDate();
        if (!occDate.isBefore(sourceStartDate) && !occDate.isAfter(sourceEndDate)) {
          ZonedDateTime sourceZDT = occ.getStartDateTime().atZone(sourceCal.getTimezone());
          Instant instant = sourceZDT.toInstant();
          LocalDateTime convertedStart = instant.atZone(targetCal.getTimezone()).toLocalDateTime();
          LocalDateTime targetStart = convertedStart.plusDays(dayOffset);
          long durationMillis = Duration.between(occ.getStartDateTime(),
              ((SingleEvent) occ).getEffectiveEndDateTime()).toMillis();
          LocalDateTime targetEnd = targetStart.plus(Duration.ofMillis(durationMillis));
          try {
            targetCal.getCalendarService().addSingleEvent(occ.getSubject(), targetStart, targetEnd,
                occ.getDescription(), occ.getLocation(), occ.isPublic(), true);
            result.append("Copied event '").append(occ.getSubject())
                .append("' to ").append(targetStart).append("\n");
          } catch (EventConflictException ex) {
            result.append("Conflict for event '").append(occ.getSubject()).append("'\n");
          }
        }
      }
    }
    if (result.length() == 0) {
      return "No events found between " + sourceStartDate + " and " + sourceEndDate;
    }
    return result.toString();
  }

  /**
   * Copies a single event or a recurring event series from one calendar to another, selecting the
   * appropriate method automatically.
   *
   * @param sourceCal   The source calendar context.
   * @param targetCal   The target calendar context.
   * @param eventName   The name of the event.
   * @param sourceStart The start time of the event or an occurrence.
   * @param targetStart The desired new start time in the target calendar.
   * @return The output, whether it threw error or events got copied.
   * @throws Exception If the event is not found or the type is unsupported.
   */
  public static String copyEvent(ICalendarContext sourceCal, ICalendarContext targetCal,
      String eventName, LocalDateTime sourceStart,
      LocalDateTime targetStart) throws Exception {
    Event found = null;
    for (Event e : sourceCal.getCalendarService().getAllEvents()) {
      if (e.getSubject().equalsIgnoreCase(eventName)) {
        for (Event occ : e.getOccurrences()) {
          if (occ.getStartDateTime().equals(sourceStart)) {
            found = e;
            break;
          }
        }
      }
      if (found != null) {
        break;
      }
    }
    if (found == null) {
      throw new Exception("Event '" + eventName + "' not found at " + sourceStart);
    }
    if (found instanceof SingleEvent) {
      return copySingleEvent(sourceCal, targetCal, eventName, sourceStart, targetStart);
    } else if (found instanceof RecurringEvent) {
      return copyRecurringEvent(sourceCal, targetCal, eventName, sourceStart, targetStart);
    } else {
      throw new Exception("Unsupported event type.");
    }
  }
}
