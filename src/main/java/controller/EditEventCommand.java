package controller;

import java.time.LocalDateTime;
import model.ICalendarService;

/**
 * Edits existing events based on a specified property change.
 */
public class EditEventCommand implements Command {

  private ICalendarService service;
  private EditMode mode;
  private String originalSubject;
  private LocalDateTime start;
  private String property;
  private String newValue;

  /**
   * Constructs a command to edit an event with minimal parameters.
   *
   * @param service  The calendar service managing events
   * @param subject  The subject of the event to edit
   * @param start    The start time of the event to edit
   * @param property The property to change
   * @param newValue The new value for the property
   * @param editMode The scope of the edit (SINGLE, FROM, or ALL)
   */
  public EditEventCommand(ICalendarService service, String subject, LocalDateTime start,
      String property, String newValue, EditMode editMode) {
    this(service, subject, start, property, newValue, editMode, false);
  }

  /**
   * Constructs a command to edit an event with all parameters.
   *
   * @param service         The calendar service managing events
   * @param originalSubject The subject of the event to edit
   * @param start           The start time of the event (null for ALL mode)
   * @param property        The property to change (e.g., "subject", "start")
   * @param newValue        The new value for the property
   * @param mode            The scope of the edit (SINGLE, FROM, or ALL)
   * @param autoDecline     Whether to auto-decline conflicting events
   */
  public EditEventCommand(ICalendarService service, String originalSubject, LocalDateTime start,
      String property, String newValue, EditMode mode, boolean autoDecline) {
    this.service = service;
    this.originalSubject = originalSubject;
    this.start = start;
    this.property = property;
    this.newValue = newValue;
    this.mode = mode;
  }

  /**
   * Constructs a command to edit all event occurrences.
   *
   * @param service         The calendar service managing events
   * @param originalSubject The subject of the event to edit
   * @param property        The property to change
   * @param newValue        The new value for the property
   * @param mode            The scope of the edit (typically ALL)
   */
  public EditEventCommand(ICalendarService service, String originalSubject, String property,
      String newValue, EditMode mode) {
    this(service, originalSubject, null, property, newValue, mode, false);
  }

  /**
   * Executes the event edit operation based on the specified mode.
   *
   * @return A message confirming the edit was applied
   * @throws Exception if an error occurs during the edit process
   */
  @Override
  public String execute() throws Exception {
    service.editEvent(originalSubject, start, property, newValue,
        ICalendarService.EditMode.valueOf(mode.name()));
    return "Edited event(s) '" + originalSubject + "': " + property + " changed to " + newValue;
  }

  /**
   * Enum defining the scope of the edit operation.
   */
  public enum EditMode {
    SINGLE, FROM, ALL
  }
}