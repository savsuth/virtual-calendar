package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents a recurring event that generates multiple single event occurrences.
 */
public class RecurringEvent extends AbstractEvent {

  private Set<DayOfWeek> recurrenceDays;
  private int occurrenceCount = -1;
  private LocalDate recurrenceEndDate;

  /**
   * Constructs a RecurringEvent with the specified parameters.
   *
   * @param subject           the event subject
   * @param startDateTime     the start date and time of the event series
   * @param endDateTime       the end date and time for each occurrence; must be on the same day as
   *                          startDateTime
   * @param description       a description of the event
   * @param location          the location of the event
   * @param isPublic          true if the event is public, false otherwise
   * @param recurrenceDays    the set of days on which the event recurs
   * @param occurrenceCount   the number of occurrences; use -1 if not specified
   * @param recurrenceEndDate the end date of the recurrence series; may be null if occurrenceCount
   *                          is specified
   * @throws InvalidDateException throws exception for invalid date
   */
  public RecurringEvent(String subject, LocalDateTime startDateTime, LocalDateTime endDateTime,
      String description, String location, boolean isPublic,
      Set<DayOfWeek> recurrenceDays,
      int occurrenceCount, LocalDate recurrenceEndDate) throws InvalidDateException {
    super(subject, startDateTime, description, location, isPublic);
    if (endDateTime.isBefore(startDateTime)
        || !startDateTime.toLocalDate().equals(endDateTime.toLocalDate())) {
      throw new InvalidDateException("End date time must be after "
          + "start date time. Start Date and End Date should be same.");
    }
    if (occurrenceCount == -1 && recurrenceEndDate == null) {
      throw new IllegalArgumentException("Either occurrence count or "
          + "recurrence end date must be provided.");
    }
    this.endDateTime = endDateTime;
    this.recurrenceDays = recurrenceDays;
    this.occurrenceCount = occurrenceCount;
    this.recurrenceEndDate = recurrenceEndDate;
  }

  /**
   * Generates a list of occurrences as single events for this recurring event.
   *
   * @return a list of SingleEvent occurrences
   */
  public List<SingleEvent> generateOccurrences() {
    List<SingleEvent> occurrences = new ArrayList<>();
    LocalDate currentDate = startDateTime.toLocalDate();
    int count = 0;
    while (true) {
      if (recurrenceDays.contains(currentDate.getDayOfWeek())) {
        LocalDateTime occurrenceStart = LocalDateTime.of(currentDate, startDateTime.toLocalTime());
        LocalDateTime occurrenceEnd = LocalDateTime.of(currentDate, endDateTime.toLocalTime());
        try {
          SingleEvent occurrence = new SingleEvent(subject, occurrenceStart,
              occurrenceEnd, description, location, isPublic);
          occurrences.add(occurrence);
        } catch (InvalidDateException e) {
          // In case of an invalid occurrence, skip to the next date.
        }
        count++;
        if (occurrenceCount != -1 && count >= occurrenceCount) {
          break;
        }
      }
      currentDate = currentDate.plusDays(1);
      if (recurrenceEndDate != null && currentDate.isAfter(recurrenceEndDate)) {
        break;
      }
    }
    return occurrences;
  }

  /**
   * Checks if this recurring event conflicts with another event by comparing each occurrence.
   *
   * @param other the other event to compare
   * @return true if any occurrence conflicts, false otherwise
   */
  @Override
  public boolean conflictsWith(Event other) {
    for (SingleEvent occurrence : this.generateOccurrences()) {
      if (other.conflictsWith(occurrence)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the list of occurrences for this recurring event.
   *
   * @return a list of event occurrences
   */
  @Override
  public List<Event> getOccurrences() {
    return new ArrayList<>(generateOccurrences());
  }


  public Set<DayOfWeek> getRecurrenceDays() {
    return recurrenceDays;
  }

  public int getOccurrenceCount() {
    return occurrenceCount;
  }

  public LocalDate getRecurrenceEndDate() {
    return recurrenceEndDate;
  }

  public void setRecurrenceEndDate(LocalDate newEndDate) {
    this.recurrenceEndDate = newEndDate;
  }


}
