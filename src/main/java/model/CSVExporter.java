package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A utility class for exporting calendar events to a CSV file.
 */
public class CSVExporter implements Exporter {

  /**
   * Writes a single event row to the CSV file.
   *
   * @param se     The event to write
   * @param writer The writer for the CSV file
   * @throws IOException If writing fails
   */
  private void writeSingleEventRow(Event se, BufferedWriter writer) throws IOException {
    LocalDateTime start = se.getStartDateTime();
    LocalDateTime end = se.getEffectiveEndDateTime();
    boolean allDay = isAllDayEvent(se);
    String privateFlag = se.isPublic() ? "false" : "true";

    StringBuilder row = new StringBuilder();
    row.append(se.getSubject()).append(",");
    row.append(start.toLocalDate()).append(",");
    row.append(start.toLocalTime()).append(",");
    row.append(end.toLocalDate()).append(",");
    row.append(end.toLocalTime()).append(",");
    row.append(allDay).append(",");
    row.append(se.getDescription()).append(",");
    row.append(se.getLocation()).append(",");
    row.append(privateFlag);

    writer.write(row.toString());
    writer.newLine();
  }

  /**
   * Checks if an event is an all-day event.
   *
   * @param se The event to check
   * @return True if the event spans a full day (midnight to 23:59), false otherwise
   */
  private boolean isAllDayEvent(Event se) {
    LocalDateTime start = se.getStartDateTime();
    LocalDateTime end = se.getEffectiveEndDateTime();
    boolean sameDate = start.toLocalDate().equals(end.toLocalDate());
    boolean startIsMidnight = (start.toLocalTime().equals(java.time.LocalTime.MIDNIGHT));
    boolean endIs2359 = (end.toLocalTime().equals(java.time.LocalTime.of(23, 59)));
    return sameDate && startIsMidnight && endIs2359;
  }

  /**
   * Exports all events from the calendar to a CSV file.
   *
   * @param calendar The calendar model to export
   * @param fileName The name of the file to create
   * @return The full path of the exported file
   * @throws IOException If the export fails
   */
  @Override
  public String export(ICalendarModel calendar, String fileName) throws IOException {
    String filePath = Paths.get(System.getProperty("user.dir"), fileName).toString();
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      writer.write("Subject,Start Date,Start Time,End Date,End Time,AllDayEvent,"
          + "Description,Location,Private");
      writer.newLine();

      List<Event> events = calendar.getAllEvents();

      for (Event event : events) {
        if (event instanceof SingleEvent) {
          Event se = event;
          writeSingleEventRow(se, writer);
        } else if (event instanceof RecurringEvent) {
          RecurringEvent re = (RecurringEvent) event;
          for (SingleEvent occurrence : re.generateOccurrences()) {
            writeSingleEventRow(occurrence, writer);
          }
        }
      }
    }
    return filePath;
  }
}

