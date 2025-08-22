package view;

import controller.IGUIController;

/**
 * Represents the GUI view interface for the calendar application. Provides methods to start the UI
 * and bind it with a controller.
 */
public interface ICalendarView {


  /**
   * Launches and displays the calendar view.
   */
  void start();

  /**
   * Sets the controller for the view.
   *
   * @param controller the controller to bind with this view
   * @throws Exception if initialization fails
   */
  void setController(IGUIController controller) throws Exception;
}
