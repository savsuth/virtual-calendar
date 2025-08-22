package view;

import controller.IGUIController;
import java.awt.Color;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.YearMonth;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * A panel displaying the calendar view of a single month. Shows day labels and events.
 */
public class MonthViewPanel extends JPanel {

  private IGUIController controller;
  private YearMonth currentYearMonth;

  /**
   * Constructor for the class.
   *
   * @param controller injected controller
   */
  public MonthViewPanel(IGUIController controller) {
    this.controller = controller;
    this.currentYearMonth = YearMonth.now();
    setLayout(new GridLayout(0, 7));
    refreshView();
  }

  /**
   * Refreshes the calendar view to reflect the current year and month. Rebuilds weekday headers and
   * day panels.
   */
  public void refreshView() {
    removeAll();

    String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    for (String dayName : weekdays) {
      JLabel label = new JLabel(dayName, SwingConstants.CENTER);
      label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      add(label);
    }

    LocalDate firstDay = currentYearMonth.atDay(1);
    int startOffset = firstDay.getDayOfWeek().getValue() % 7;
    for (int i = 0; i < startOffset; i++) {
      add(new JPanel());
    }

    int daysInMonth = currentYearMonth.lengthOfMonth();
    for (int day = 1; day <= daysInMonth; day++) {
      DayPanel dp = new DayPanel(day, currentYearMonth, controller);
      add(dp);
    }
    revalidate();
    repaint();
  }

  /**
   * Advances the view to the next month and refreshes the panel.
   */
  public void nextMonth() {
    currentYearMonth = currentYearMonth.plusMonths(1);
    refreshView();
  }

  /**
   * Goes back to the previous month and refreshes the panel.
   */
  public void previousMonth() {
    currentYearMonth = currentYearMonth.minusMonths(1);
    refreshView();
  }


  /**
   * Returns the current YearMonth displayed in this panel.
   *
   * @return the current YearMonth
   */
  public YearMonth getCurrentYearMonth() {
    return currentYearMonth;
  }
}
