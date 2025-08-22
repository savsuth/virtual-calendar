package view;

import controller.CalendarControllerFactory;
import controller.IAppController;
import controller.ICalendarControllerFactory;
import model.CalendarManager;
import model.ICalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;

/**
 * The main application class for the Calendar App. Supports both interactive and headless execution
 * modes.
 */
public class CalendarApp {

  /**
   * Entry point for the Calendar application.
   *
   * @param args command line arguments: --mode [interactive|headless] [optional command file]
   */
  public static void main(String[] args) throws Exception {

    ICalendarManager calendarManager = new CalendarManager();
    IMultiCalendarService service = new MultiCalendarService(calendarManager);

    ICalendarView view = new CalendarGUI();

    ICalendarControllerFactory factory = new CalendarControllerFactory();
    IAppController controller = factory.createController(args, service, view);

    controller.start();
  }
}

