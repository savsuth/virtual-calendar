package model;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Provides services for managing events in a calendar model.
 */
public class CalendarService implements ICalendarService {

  private ICalendarModel model;
  private Exporter exporter;
  private IEventPrinter printer;
  private IEditEventOperations editOps;

  /**
   * Constructs a service instance tied to a specific calendar model.
   *
   * @param model The calendar model to manage
   */
  public CalendarService(ICalendarModel model) {
    this.model = model;
    this.exporter = new CSVExporter();
    this.printer = new EventPrinter(model);
    this.editOps = new EditEventOperations();
  }

  /**
   * Adds a single event to the calendar.
   *
   * @param subject     The event title
   * @param start       The start date-time
   * @param end         The end date-time
   * @param description The event description
   * @param location    The event location
   * @param isPublic    Whether the event is public
   * @param autoDecline Whether to auto-decline conflicts
   * @throws InvalidDateException   If the dates are invalid
   * @throws EventConflictException If there’s a conflict and auto-decline is enabled
   */
  @Override
  public void addSingleEvent(String subject, LocalDateTime start, LocalDateTime end,
      String description, String location,
      boolean isPublic, boolean autoDecline)
      throws InvalidDateException, EventConflictException {
    AbstractEvent event = new SingleEvent(subject, start, end, description, location, isPublic);
    event.setAutoDecline(autoDecline);
    model.addEvent(event, autoDecline);
  }

  /**
   * Adds a recurring event to the calendar.
   *
   * @param subject           The event title
   * @param start             The start date-time of the first occurrence
   * @param end               The end date-time of each occurrence
   * @param description       The event description
   * @param location          The event location
   * @param isPublic          Whether the event is public
   * @param recurrenceDays    The days of the week the event repeats on
   * @param occurrenceCount   The number of occurrences (-1 for indefinite)
   * @param recurrenceEndDate The end date of the recurrence (null for no end)
   * @param autoDecline       Whether to auto-decline conflicts
   * @throws InvalidDateException   If the dates are invalid
   * @throws EventConflictException If there’s a conflict and auto-decline is enabled
   */
  @Override
  public void addRecurringEvent(String subject, LocalDateTime start, LocalDateTime end,
      String description, String location, boolean isPublic,
      java.util.Set<java.time.DayOfWeek> recurrenceDays,
      int occurrenceCount, LocalDate recurrenceEndDate,
      boolean autoDecline)
      throws InvalidDateException, EventConflictException {
    AbstractEvent event = new RecurringEvent(subject, start, end, description, location, isPublic,
        recurrenceDays, occurrenceCount, recurrenceEndDate);
    event.setAutoDecline(autoDecline);
    model.addEvent(event, autoDecline);
  }

  /**
   * Gets all events occurring on a specific date.
   *
   * @param date The date to check
   * @return A list of events on that date
   */
  @Override
  public List<Event> getEventsOn(LocalDate date) {
    return model.getEventsOn(date);
  }

  /**
   * Gets all events in the calendar.
   *
   * @return A list of all events
   */
  @Override
  public List<Event> getAllEvents() {
    return model.getAllEvents();
  }

  /**
   * Checks if the calendar is busy at a specific time.
   *
   * @param dateTime The time to check
   * @return True if an event overlaps the time, false otherwise
   */
  @Override
  public boolean isBusyAt(LocalDateTime dateTime) {
    return model.isBusyAt(dateTime);
  }

  /**
   * Edits an event’s property based on the specified mode.
   *
   * @param subject  The subject of the event to edit
   * @param from     The start time for editing (null for ALL mode)
   * @param property The property to change
   * @param newValue The new value for the property
   * @param mode     The edit mode (SINGLE, FROM, ALL)
   * @throws Exception If the edit fails
   */
  @Override
  public void editEvent(String subject, LocalDateTime from, String property, String newValue,
      EditMode mode) throws Exception {
    editOps.editEvent(model, subject, from, property, newValue, mode);
  }

  /**
   * Exports the calendar to a CSV file.
   *
   * @param format The export format (currently only "csv" is supported)
   * @param path   The file path to export to
   * @return The path where the file was saved
   * @throws IOException If the export fails
   */
  @Override
  public String exportTo(String format, String path) throws IOException {
    exporter = new ExportFactory().getExport(format);
    return exporter.export(model, path);
  }

  @Override
  public String importFrom(String format, String path) throws Exception {
    Importer importer = new ImportFactory().getImporter(format);
    return importer.importData(model, path);
  }

  /**
   * Prints events for a specific date.
   *
   * @param date The date to print events for
   * @return A formatted string of events
   * @throws Exception If printing fails
   */
  @Override
  public String printEventsOn(LocalDate date) throws Exception {
    return printer.printEventsOn(date);
  }

  /**
   * Prints events within a date-time range.
   *
   * @param start The start of the range
   * @param end   The end of the range
   * @return A formatted string of events
   * @throws Exception If printing fails
   */
  @Override
  public String printEventsRange(LocalDateTime start, LocalDateTime end) throws Exception {
    return printer.printEventsRange(start, end);
  }
}