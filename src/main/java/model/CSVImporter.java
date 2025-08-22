package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Imports calendar events from a CSV file into the model.
 */
public class CSVImporter implements Importer {

  /**
   * Parses event from CSV token data.
   *
   * @param tokens the CSV fields
   * @return the parsed {@link SingleEvent}
   * @throws InvalidDateException if date or time values are invalid
   */
  private static SingleEvent getSingleEvent(String[] tokens) throws InvalidDateException {
    String subject = tokens[0].trim();
    String startDateStr = tokens[1].trim();
    String startTimeStr = tokens[2].trim();
    String endDateStr = tokens[3].trim();
    String endTimeStr = tokens[4].trim();
    // String allDayStr = tokens[5].trim(); // Not using this for import, no need.
    String description = tokens[6].trim();
    String location = tokens[7].trim();
    String privateFlagStr = tokens[8].trim();


    LocalDate startDate = LocalDate.parse(startDateStr);
    LocalTime startTime = LocalTime.parse(startTimeStr);
    LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);

    LocalDate endDate = LocalDate.parse(endDateStr);
    LocalTime endTime = LocalTime.parse(endTimeStr);
    LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

    boolean isPublic = !"true".equalsIgnoreCase(privateFlagStr);


    SingleEvent event = new SingleEvent(subject, startDateTime, endDateTime,
        description, location, isPublic);
    return event;
  }

  @Override
  public String importData(ICalendarModel model, String fileName) throws IOException {
    int importedCount = 0;
    int errorCount = 0;
    StringBuilder errorMessages = new StringBuilder();


    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String headerLine = reader.readLine();
      if (headerLine == null) {
        return "Error: CSV file is empty.";
      }

      String line;
      int lineNumber = 1;
      while ((line = reader.readLine()) != null) {
        lineNumber++;

        String[] tokens = line.split(",", -1);
        if (tokens.length < 9) {
          errorMessages.append("Line ").append(lineNumber)
              .append(": Invalid number of fields.\n");
          errorCount++;
          continue;
        }
        try {

          SingleEvent event = getSingleEvent(tokens);
          event.setAutoDecline(true);
          model.addEvent(event, true);
          importedCount++;
        } catch (Exception e) {
          errorMessages.append("Line ").append(lineNumber)
              .append(": ").append(e.getMessage()).append("\n");
          errorCount++;
        }
      }
    }
    String result = "Imported " + importedCount + " events.";
    if (errorCount > 0) {
      result += "\n" + errorCount + " errors:\n" + errorMessages.toString();
    }
    return result;
  }
}
