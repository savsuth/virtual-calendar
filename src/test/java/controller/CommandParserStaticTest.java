package controller;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import model.InvalidDateException;
import org.junit.Test;

/**
 * Unit test for the static date-time parsing method in CommandParserStatic.
 */
public class CommandParserStaticTest {

  @Test
  public void testParseDateTimeStatic() throws InvalidDateException {
    String input = "2025-03-01T09:00";
    LocalDateTime dateTime = CommandParserStatic.parseDateTimeStatic(input);
    assertEquals(2025, dateTime.getYear());
    assertEquals(3, dateTime.getMonthValue());
    assertEquals(1, dateTime.getDayOfMonth());
    assertEquals(9, dateTime.getHour());
    assertEquals(0, dateTime.getMinute());
  }

}