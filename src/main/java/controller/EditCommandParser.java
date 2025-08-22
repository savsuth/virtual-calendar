package controller;

import java.time.LocalDateTime;
import model.ICalendarService;

/**
 * Parses edit commands and creates commands to update existing events.
 */
public class EditCommandParser implements ICommandParser {

  private ICalendarService service;

  /**
   * Constructs a parser with the given calendar service.
   *
   * @param service The service managing calendar events
   */
  public EditCommandParser(ICalendarService service) {
    this.service = service;
  }

  /**
   * Parses command tokens into an event editing Command.
   *
   * @param tokens The array of strings from the command input
   * @return A Command object for editing events, or an error command if parsing fails
   */
  @Override
  public Command parse(String[] tokens) {
    try {
      int index = 1;
      if (!(tokens[index].equalsIgnoreCase("event")
          || tokens[index].equalsIgnoreCase("events"))) {
        return () -> "Invalid edit command.";
      }
      String type = tokens[index];
      index++;
      boolean autoDecline = false;
      if (index < tokens.length && tokens[index].equalsIgnoreCase("--autoDecline")) {
        autoDecline = true;
        index++;
      }
      if (type.equalsIgnoreCase("event")) {
        String property = tokens[index++];
        String subject = tokens[index++];
        if (!tokens[index].equalsIgnoreCase("from")) {
          return () -> "Expected 'from' in edit event command.";
        }
        index++;
        String dateTimeStr = tokens[index++];
        LocalDateTime targetTime = CommandParserStatic.parseDateTimeStatic(dateTimeStr);
        int withIndex = -1;
        for (int i = 0; i < tokens.length; i++) {
          if (tokens[i].equalsIgnoreCase("with")) {
            withIndex = i;
            break;
          }
        }
        if (withIndex == -1 || withIndex + 1 >= tokens.length) {
          return () -> "Edit command missing 'with' and new value.";
        }
        String newValue = tokens[withIndex + 1];
        return new EditEventCommand(service, subject, targetTime, property, newValue,
            EditEventCommand.EditMode.SINGLE, autoDecline);
      } else if (tokens[1].equalsIgnoreCase("events")) {
        String property = tokens[index++];
        String subject = tokens[index++];
        EditEventCommand.EditMode mode;
        LocalDateTime fromTime = null;
        String newValue = null;
        if (index < tokens.length && tokens[index].equalsIgnoreCase("from")) {
          mode = EditEventCommand.EditMode.FROM;
          index++;
          String dateTimeStr = tokens[index++];
          fromTime = CommandParserStatic.parseDateTimeStatic(dateTimeStr);
          int withIndex = -1;
          for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equalsIgnoreCase("with")) {
              withIndex = i;
              break;
            }
          }
          if (withIndex == -1 || withIndex + 1 >= tokens.length) {
            return () -> "Edit command missing 'with' and new value.";
          }
          newValue = tokens[withIndex + 1];
        } else {
          mode = EditEventCommand.EditMode.ALL;
          int withIndex = -1;
          for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equalsIgnoreCase("with")) {
              withIndex = i;
              break;
            }
          }
          if (withIndex == -1 || withIndex + 1 >= tokens.length) {
            return () -> "Edit command missing 'with' and new value.";
          }
          newValue = tokens[withIndex + 1];
        }
        return new EditEventCommand(service, subject,
            fromTime, property, newValue, mode, autoDecline);
      } else {
        return () -> "Invalid edit command.";
      }
    } catch (Exception e) {
      return () -> "Error processing edit command: " + e.getMessage();
    }
  }
}