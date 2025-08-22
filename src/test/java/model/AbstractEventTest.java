package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import org.junit.Test;

/**
 * JUnit Test Case for {@link AbstractEvent} class. Tests the functionality which is shared both by
 * {@code RecurringEvent & SingleEvent}.
 */

public class AbstractEventTest {

  @Test
  public void getDescription() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event = new SingleEvent("Meeting", start, end, "Test Description", "Room101", true);
    assertEquals("Test Description", event.getDescription());
  }

  @Test
  public void testGetSubject() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event = new SingleEvent("Meeting", start, end, "Desc", "Room101", true);
    assertEquals("Meeting", event.getSubject());
  }

  @Test
  public void testGetStartDateTime() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event = new SingleEvent("Meeting", start, end, "Desc", "Room101", true);
    assertEquals(start, event.getStartDateTime());
  }

  @Test
  public void testGetLocation() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event = new SingleEvent("Meeting", start, end, "Desc", "Room101", true);
    assertEquals("Room101", event.getLocation());
  }

  @Test
  public void testIsPublic() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent eventTrue = new SingleEvent("Meeting", start, end, "Desc", "Room101", true);
    SingleEvent eventFalse = new SingleEvent("Meeting", start, end, "Desc", "Room101", false);
    assertTrue(eventTrue.isPublic());
    assertFalse(eventFalse.isPublic());
  }

  @Test
  public void testIsAutoDecline() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event = new SingleEvent("Meeting", start, end, "Desc", "Room101", true);
    assertFalse(event.isAutoDecline());
  }

  @Test
  public void testSetAutoDecline() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event = new SingleEvent("Meeting", start, end, "Desc", "Room101", true);
    event.setAutoDecline(true);
    assertTrue(event.isAutoDecline());
  }

  @Test
  public void testSetSubject() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event = new SingleEvent("Meeting", start, end, "Desc", "Room101", true);
    event.setSubject("Team Meeting");
    assertEquals("Team Meeting", event.getSubject());
  }

  @Test
  public void testSetDescription() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event = new SingleEvent("Meeting", start, end, "Desc", "Room101", true);
    event.setDescription("Updated Description");
    assertEquals("Updated Description", event.getDescription());
  }

  @Test
  public void testSetLocation() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event = new SingleEvent("Meeting", start, end, "Desc", "Room101", true);
    event.setLocation("Room202");
    assertEquals("Room202", event.getLocation());
  }

  @Test
  public void testSetStartDateTime() throws InvalidDateException {
    LocalDateTime originalStart = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime newStart = LocalDateTime.of(2025, 3, 1, 8, 30);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event = new SingleEvent("Meeting", originalStart, end, "Desc", "Room101", true);
    event.setStartDateTime(newStart);
    assertEquals(newStart, event.getStartDateTime());
  }

  @Test
  public void testSetPublic() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event = new SingleEvent("Meeting", start, end, "Desc", "Room101", true);

    assertTrue(event.isPublic());
    event.setPublic(false);
    assertFalse(event.isPublic());
  }

  @Test
  public void testGetEffectiveEndDateTime() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event1 = new SingleEvent("Meeting", start, end, "Desc", "Room101", true);
    assertEquals(end, event1.getEffectiveEndDateTime());

    SingleEvent event2 = new SingleEvent("Meeting", start, end, "Desc", "Room101", true);
    event2.setEndDateTime(null);
    LocalDateTime expected = start.toLocalDate().atTime(23, 59);
    assertEquals(expected, event2.getEffectiveEndDateTime());
  }

  @Test
  public void testSetEndDateTime() throws InvalidDateException {
    LocalDateTime start = LocalDateTime.of(2025, 3, 1, 9, 0);
    LocalDateTime originalEnd = LocalDateTime.of(2025, 3, 1, 10, 0);
    SingleEvent event = new SingleEvent("Meeting", start, originalEnd, "Desc", "Room101", true);

    LocalDateTime newEnd = LocalDateTime.of(2025, 3, 1, 11, 0);
    event.setEndDateTime(newEnd);
    assertEquals(newEnd, event.getEffectiveEndDateTime());

    try {
      event.setEndDateTime(LocalDateTime.of(2025, 3, 1, 8, 0));
      fail("Expected InvalidDateException not thrown.");
    } catch (InvalidDateException e) {
      assertEquals("End date & time must be after start date & time.", e.getMessage());
    }
  }
}