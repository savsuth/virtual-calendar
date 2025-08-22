package model;

import java.time.LocalDateTime;

/**
 * Defines operations for editing an event in the calendar.
 */
public interface IEditEventOperations {

  /**
   * Edits an event's property.
   *
   * @param model    the calendar model
   * @param subject  the subject of the event to edit
   * @param from     the original start time
   * @param property the property to update
   * @param newValue the new value to set
   * @param mode     the scope of the edit (e.g., SINGLE, FROM, ALL)
   * @throws Exception if editing fails
   */
  void editEvent(ICalendarModel model,
      String subject,
      LocalDateTime from,
      String property,
      String newValue,
      ICalendarService.EditMode mode) throws Exception;
}
