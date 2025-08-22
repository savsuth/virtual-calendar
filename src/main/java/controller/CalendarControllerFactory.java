package controller;

import model.IMultiCalendarService;
import view.ICalendarView;

/**
 * A factory that encapsulates the creation and startup of the appropriate calendar controller.
 */
public class CalendarControllerFactory implements ICalendarControllerFactory {


  @Override
  public IAppController createController(String[] args, IMultiCalendarService service,
      ICalendarView view) throws Exception {
    IAppController controller;
    if (args == null || args.length == 0) {
      controller = new GUIController(service, view);
    } else if (args.length > 0 && args[0].equalsIgnoreCase("--mode")) {
      String mode = args[1].toLowerCase();
      if (mode.equals("interactive") || mode.equals("headless")) {
        controller = new TextBasedController(service, args);
      } else {
        System.out.println("Unknown mode specified, defaulting to GUI.");
        controller = new GUIController(service, view);
      }
    } else {
      controller = new GUIController(service, view);
    }

    if (controller instanceof IGUIController) {
      view.setController((IGUIController) controller);
    }

    return controller;
  }
}
