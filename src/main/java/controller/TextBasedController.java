package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import model.IMultiCalendarService;

/**
 * Text-based controller for the calendar application.
 * Supports interactive and headless modes based on command-line arguments.
 */
public class TextBasedController extends ExtendedCalendarController implements IAppController {

  private String[] args;

  /**
   * Constructs the controller with the given service and arguments.
   *
   * @param service the calendar service
   * @param args    command-line arguments
   */
  public TextBasedController(IMultiCalendarService service, String[] args) {
    super(service);
    this.args = args;
  }

  @Override
  public void start() {
    if (args.length >= 2 && args[0].equalsIgnoreCase("--mode")) {
      String mode = args[1].toLowerCase();
      if (mode.equals("interactive")) {
        runInteractive();
      } else if (mode.equals("headless")) {
        if (args.length < 3) {
          System.out.println("Headless mode requires a command file.");
          return;
        }
        runHeadless(args[2]);
      } else {
        System.out.println("Unknown mode: " + mode);
      }
    } else {
      System.out.println("Invalid arguments for text mode.");
    }
  }

  private void runInteractive() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Interactive Calendar App. Type 'exit' to quit.");
    while (true) {
      System.out.print("> ");
      String line = scanner.nextLine();
      String result = processCommand(line);
      if ("exit".equals(result)) {
        break;
      }
      System.out.println(result);
    }
    scanner.close();
  }

  private void runHeadless(String fileName) {
    try (Scanner scanner = new Scanner(new File(fileName))) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String result = processCommand(line);
        if ("exit".equals(result)) {
          break;
        }
        System.out.println(result);
      }
    } catch (FileNotFoundException e) {
      System.out.println("Command file not found: " + fileName);
    }
  }
}
