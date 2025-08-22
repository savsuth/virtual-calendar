package controller;

import java.time.LocalDate;
import model.IMultiCalendarService;
import view.ICalendarView;

/**
 * Controller implementation that handles user actions from the GUI.
 * Delegates requests to the service layer and returns results or error messages.
 */
public class GUIController implements IGUIController {

  private IMultiCalendarService service;
  private ICalendarView view;

  /**
   * Constructs a GUIController with the given service and view.
   *
   * @param service the calendar service layer
   * @param view    the GUI view interface
   */
  public GUIController(IMultiCalendarService service, ICalendarView view) {
    this.service = service;
    this.view = view;
  }

  @Override
  public String useCalendar(String calendarName) {
    GUIUseCalendarParser parser = new GUIUseCalendarParser(service);
    Command cmd = parser.parse(calendarName);
    try {
      return cmd.execute();
    } catch (Exception e) {
      return "Error switching calendar: " + e.getMessage();
    }
  }

  @Override
  public String createCalendar(String calendarName, String timezone) {
    GUICreateCalendarParser parser = new GUICreateCalendarParser(service);
    Command cmd = parser.parse(calendarName, timezone);
    try {
      return cmd.execute();
    } catch (Exception e) {
      return "Error creating calendar: " + e.getMessage();
    }
  }

  @Override
  public String createSingleEvent(String subject, String start, String end,
      String description, String location, boolean isPublic, boolean autoDecline) {
    GUICreateSingleEventParser parser = new GUICreateSingleEventParser(service);
    Command cmd = parser.parse(subject, start, end, description, location, isPublic, autoDecline);
    try {
      return cmd.execute();
    } catch (Exception e) {
      return "Error creating event: " + e.getMessage();
    }
  }

  @Override
  public String createRecurringEvent(String subject, String start, String end,
      String description, String location, boolean isPublic,
      String recurrenceDays, String occurrenceCount,
      String recurrenceEndDate, boolean autoDecline) {
    GUICreateRecurringEventParser parser = new GUICreateRecurringEventParser(service);
    Command cmd = parser.parse(subject, start, end, description, location, isPublic, recurrenceDays,
        occurrenceCount, recurrenceEndDate, autoDecline);
    try {
      return cmd.execute();
    } catch (Exception e) {
      return "Error creating recurring event: " + e.getMessage();
    }
  }

  @Override
  public String editEvent(String subject, String from, String property, String newValue,
      String mode) {
    GUIEditEventParser parser = new GUIEditEventParser(service);
    Command cmd = parser.parse(subject, from, property, newValue, mode);
    try {
      return cmd.execute();
    } catch (Exception e) {
      return "Error editing event: " + e.getMessage();
    }
  }

  @Override
  public String printEventsOn(LocalDate date) {
    GUIPrintEventsParser parser = new GUIPrintEventsParser(service);
    Command cmd = parser.parse(date);
    try {
      return cmd.execute();
    } catch (Exception e) {
      return "Error printing events: " + e.getMessage();
    }
  }

  @Override
  public String exportCalendar(String fileName, String format) {
    try {
      IGUIExporterParser parser = GUIExportFactory.getParser(format, service);
      Command cmd = parser.parse(fileName);
      return cmd.execute();
    } catch (Exception e) {
      return "Error exporting calendar: " + e.getMessage();
    }
  }

  @Override
  public String importCalendar(String fileName) {
    try {

      String format = "csv";
      int dotIndex = fileName.lastIndexOf('.');
      if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
        format = fileName.substring(dotIndex + 1).toLowerCase();
      }
      IGUIImporterParser parser = GUIImportFactory.getParser(format, service);

      Command cmd = parser.parse(fileName);
      return cmd.execute();
    } catch (Exception e) {
      return "Error importing calendar: " + e.getMessage();
    }

  }

  @Override
  public String editCalendar(String calendarName, String property, String newValue) {
    GUIEditCalendarParser parser = new GUIEditCalendarParser(service);
    Command cmd = parser.parse(calendarName, property, newValue);
    try {
      return cmd.execute();
    } catch (Exception e) {
      return "Error editing calendar: " + e.getMessage();
    }
  }

  @Override
  public String[] getCurrentCalendarNameAndZone() throws Exception {
    return service.getCurrentCalendarNameAndZone();
  }


  @Override
  public void start() {
    view.start();
  }

}
