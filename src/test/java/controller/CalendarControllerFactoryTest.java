package controller;

import static org.junit.Assert.assertTrue;

import model.CalendarManager;
import model.IMultiCalendarService;
import model.MultiCalendarService;
import org.junit.Test;
import view.ICalendarView;

/**
 * Tests the CalendarControllerFactory to ensure it returns the correct controller type based on the
 * provided arguments.
 */
public class CalendarControllerFactoryTest {

  @Test
  public void createController() throws Exception {
    CalendarControllerFactory factory = new CalendarControllerFactory();
    IMultiCalendarService service = new MultiCalendarService(new CalendarManager());
    ICalendarView view = new ICalendarView() {
      @Override
      public void start() {
        // View is needed by CalendarFactory.
        // Cannot use CalendarGUI here(pitest doesn't work if I do).
      }

      @Override
      public void setController(IGUIController controller) throws Exception {
        // Interface necessitates to use this methods.
      }
    };

    IAppController controller = factory.createController(null, service, view);
    assertTrue(controller instanceof GUIController);

    controller = factory.createController(new String[0], service, view);
    assertTrue(controller instanceof GUIController);

    controller = factory.createController(new String[]{"--mode", "interactive"}, service, view);
    assertTrue(controller instanceof TextBasedController);

    controller = factory.createController(new String[]{"--mode", "headless"}, service, view);
    assertTrue(controller instanceof TextBasedController);

    controller = factory.createController(new String[]{"--mode", "unknown"}, service, view);
    assertTrue(controller instanceof GUIController);

    controller = factory.createController(new String[]{"other"}, service, view);
    assertTrue(controller instanceof GUIController);
  }

}