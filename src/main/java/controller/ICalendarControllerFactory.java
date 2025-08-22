package controller;

import model.IMultiCalendarService;
import view.ICalendarView;

/**
 * An interface for a factory that creates and starts the appropriate calendar controller.
 */
public interface ICalendarControllerFactory {

  IAppController createController(String[] args, IMultiCalendarService service, ICalendarView view)
      throws Exception;
}
