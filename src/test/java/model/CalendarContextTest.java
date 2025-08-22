package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.time.DateTimeException;
import java.time.ZoneId;
import org.junit.Test;

/**
 * Tests for the {@link CalendarContext} class. Checks different scenarios such as Invalid
 * TimeZones, Empty Strings, Change of TimeZones etc.
 */
public class CalendarContextTest {

  @Test
  public void testGettersAndSetters() {
    CalendarContext context = new CalendarContext("InitialName", "America/New_York");
    assertEquals("InitialName", context.getName());
    assertEquals(ZoneId.of("America/New_York"), context.getTimezone());
    context.setName("UpdatedName");
    assertEquals("UpdatedName", context.getName());
    context.setTimezone("Europe/London");
    assertEquals(ZoneId.of("Europe/London"), context.getTimezone());
    assertNotNull(context.getCalendarModel());
    assertNotNull(context.getCalendarService());
  }

  @Test(expected = DateTimeException.class)
  public void testInvalidTimezoneThrowsException() {
    new CalendarContext("Test", "Invalid/Timezone");
  }

  @Test(expected = DateTimeException.class)
  public void testSetTimezoneInvalidValue() {
    CalendarContext context = new CalendarContext("Test", "America/New_York");
    context.setTimezone("Not/A_Timezone");
  }

  @Test
  public void testSetNameToEmptyString() {
    CalendarContext context = new CalendarContext("NonEmpty", "America/New_York");
    context.setName("");
    assertEquals("", context.getName());
  }

  @Test
  public void testTimezoneConversionNotNull() {
    CalendarContext context = new CalendarContext("CalTest", "Asia/Tokyo");
    assertNotNull(context.getTimezone());
    assertEquals(ZoneId.of("Asia/Tokyo"), context.getTimezone());
  }

  @Test
  public void testMultipleTimezoneChanges() {
    CalendarContext context = new CalendarContext("MultiTZ", "America/Los_Angeles");
    context.setTimezone("Europe/Berlin");
    assertEquals(ZoneId.of("Europe/Berlin"), context.getTimezone());
    context.setTimezone("Australia/Sydney");
    assertEquals(ZoneId.of("Australia/Sydney"), context.getTimezone());
    context.setTimezone("Africa/Cairo");
    assertEquals(ZoneId.of("Africa/Cairo"), context.getTimezone());
  }

  @Test
  public void testDistinctInstancesBetweenContexts() {
    CalendarContext context1 = new CalendarContext("Cal1", "America/New_York");
    CalendarContext context2 = new CalendarContext("Cal2", "Europe/London");
    assertNotSame(context1.getCalendarModel(), context2.getCalendarModel());
    assertNotSame(context1.getCalendarService(), context2.getCalendarService());
  }

  @Test
  public void testCalendarServiceConsistency() {
    CalendarContext context = new CalendarContext("Test", "America/New_York");
    assertSame(context.getCalendarService(), context.getCalendarService());
  }

  @Test
  public void testCalendarModelConsistency() {
    CalendarContext context = new CalendarContext("Test", "America/New_York");
    assertSame(context.getCalendarModel(), context.getCalendarModel());
  }

  @Test
  public void testCalendarModelServiceIntegrityAfterChanges() {
    CalendarContext context = new CalendarContext("IntegrityTest", "America/New_York");
    context.setName("IntegrityUpdated");
    context.setTimezone("Europe/Paris");
    assertNotNull(context.getCalendarModel());
    assertNotNull(context.getCalendarService());
  }
}