package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link CalendarManager} class.
 */
public class CalendarManagerTest {

  private CalendarManager manager;

  @Before
  public void setUp() {
    manager = new CalendarManager();
  }

  @Test
  public void testAddAndGetCalendar() {
    ICalendarContext cal1 = new CalendarContext("Cal1", "America/New_York");
    assertTrue("Calendar should be added successfully.", manager.addCalendar(cal1));
    ICalendarContext fetched = manager.getCalendar("Cal1");
    assertNotNull("Calendar should be retrievable by name.", fetched);
    assertEquals("Cal1", fetched.getName());
  }

  @Test
  public void testAddDuplicateCalendar() {
    ICalendarContext cal1 = new CalendarContext("Cal1", "America/New_York");
    ICalendarContext cal2 = new CalendarContext("Cal1", "Europe/Paris");
    assertTrue("First calendar added successfully.", manager.addCalendar(cal1));
    assertFalse("Adding duplicate calendar should fail.", manager.addCalendar(cal2));
  }

  @Test
  public void testSetAndGetCurrentCalendar() {
    ICalendarContext cal1 = new CalendarContext("Cal1", "America/New_York");
    ICalendarContext cal2 = new CalendarContext("Cal2", "Europe/Paris");
    manager.addCalendar(cal1);
    manager.addCalendar(cal2);
    manager.setCurrentCalendar("Cal2");
    ICalendarContext current = manager.getCurrentCalendar();
    assertNotNull("Current calendar should not be null.", current);
    assertEquals("Cal2", current.getName());
  }

  @Test
  public void testRemoveCalendar() {
    ICalendarContext cal1 = new CalendarContext("Cal1", "America/New_York");
    manager.addCalendar(cal1);
    manager.removeCalendar("Cal1");
    assertNull("Calendar should be removed.", manager.getCalendar("Cal1"));
    assertNull("Current calendar should be null after removal.",
        manager.getCurrentCalendar());
  }

  @Test
  public void testSetCurrentCalendarNonExistent() {
    ICalendarContext cal1 = new CalendarContext("Cal1", "America/New_York");
    manager.addCalendar(cal1);
    manager.setCurrentCalendar("Cal1");
    ICalendarContext current = manager.getCurrentCalendar();
    assertNotNull("Current calendar should not be null.", current);
    manager.setCurrentCalendar("NonExistent");
    ICalendarContext stillCurrent = manager.getCurrentCalendar();
    assertEquals("Current calendar should remain unchanged when" +
            " non-existent calendar is specified.",
        "Cal1", stillCurrent.getName());
  }

  @Test
  public void testRemoveCalendarWhenMultipleExist() {
    ICalendarContext cal1 = new CalendarContext("Cal1", "America/New_York");
    ICalendarContext cal2 = new CalendarContext("Cal2", "Europe/Paris");
    manager.addCalendar(cal1);
    manager.addCalendar(cal2);
    manager.setCurrentCalendar("Cal2");
    manager.removeCalendar("Cal1");
    assertNull("Cal1 should be removed.", manager.getCalendar("Cal1"));
    assertNotNull("Current calendar should still exist.", manager.getCurrentCalendar());
    assertEquals("Cal2", manager.getCurrentCalendar().getName());
  }

  @Test
  public void testAddMultipleDifferentCalendars() {
    ICalendarContext cal1 = new CalendarContext("Work", "America/New_York");
    ICalendarContext cal2 = new CalendarContext("Personal", "Europe/Paris");
    ICalendarContext cal3 = new CalendarContext("Holidays", "Asia/Tokyo");

    assertTrue(manager.addCalendar(cal1));
    assertTrue(manager.addCalendar(cal2));
    assertTrue(manager.addCalendar(cal3));
    assertNotNull(manager.getCalendar("Work"));
    assertNotNull(manager.getCalendar("Personal"));
    assertNotNull(manager.getCalendar("Holidays"));
  }
}
