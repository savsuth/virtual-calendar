package controller;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import model.CalendarManager;
import model.ICalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link TextBasedController} class, which is for CLI arguments.
 */
public class TextBasedControllerTest {

  private final PrintStream originalOut = System.out;
  private final InputStream originalIn = System.in;
  private ByteArrayOutputStream outContent;

  private IMultiCalendarService service;

  /**
   * Sets up the calendar service and redirects System.out for output capture.
   */
  @Before
  public void setUp() throws Exception {

    outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    ICalendarManager manager = new CalendarManager();
    service = new MultiCalendarService(manager);

    service.createCalendar("Default", "UTC");
    service.useCalendar("Default");
  }

  @After
  public void tearDown() throws Exception {
    System.setOut(originalOut);
    System.setIn(originalIn);
  }

  @Test
  public void testInvalidArguments() {

    String[] args = {};
    TextBasedController controller = new TextBasedController(service, args);
    controller.start();
    String output = outContent.toString();
    assertTrue(output.contains("Invalid arguments for text mode."));
  }

  @Test
  public void testHeadlessNoCommandFile() {

    String[] args = {"--mode", "headless"};
    outContent.reset();
    TextBasedController controller = new TextBasedController(service, args);
    controller.start();
    String output = outContent.toString();
    assertTrue(output.contains("Headless mode requires a command file."));
  }

  @Test
  public void testHeadlessFileNotFound() {

    String nonExistentPath = Paths.get(System.getProperty("user.dir"), "nonexistentfile.txt")
        .toString();
    String[] args = {"--mode", "headless", nonExistentPath};
    outContent.reset();
    TextBasedController controller = new TextBasedController(service, args);
    controller.start();
    String output = outContent.toString();
    assertTrue(output.contains("Command file not found: " + nonExistentPath));
  }

  @Test
  public void testHeadlessWithCommands() throws Exception {

    File tempFile = File.createTempFile("commands", ".txt");
    tempFile.deleteOnExit();
    try (FileWriter writer = new FileWriter(tempFile)) {
      writer.write("unknowncommand\n");
      writer.write("exit\n");
    }
    String filePath = tempFile.getAbsolutePath();
    String[] args = {"--mode", "headless", filePath};
    outContent.reset();
    TextBasedController controller = new TextBasedController(service, args);
    controller.start();
    String output = outContent.toString();

    assertTrue(output.contains("Unknown command."));
    tempFile.delete();
  }

  @Test
  public void testInteractiveMode() {
    String inp = "exit\n";
    ByteArrayInputStream inContent = new ByteArrayInputStream(inp.getBytes(
        StandardCharsets.UTF_8));
    System.setIn(inContent);

    String[] args = {"--mode", "interactive"};
    outContent.reset();
    TextBasedController controller = new TextBasedController(service, args);
    controller.start();
    String output = outContent.toString();

    assertTrue(output.contains("Interactive Calendar App. Type 'exit' to quit."));
    assertTrue(output.contains("> "));
  }
}