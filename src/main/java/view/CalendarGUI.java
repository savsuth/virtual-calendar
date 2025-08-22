package view;

import controller.IGUIController;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.YearMonth;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Represents the main GUI window for the calendar application.
 * Handles navigation, event creation, import/export, and calendar display.
 */
public class CalendarGUI extends JFrame implements ICalendarView {

  private IGUIController controller;


  private MonthViewPanel monthViewPanel;
  private JLabel monthLabel;
  private JLabel calendarInfoLabel;

  private final Runnable refreshMonthViewRunnable = () -> {
    if (monthViewPanel != null) {
      monthViewPanel.refreshView();
      updateMonthLabel();
      try {
        updateCalendarInfo();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  };

  /**
   * Constructs the main calendar GUI frame.
   * Initializes window settings but not components.
   */
  public CalendarGUI() {
    setTitle("Calendar Application");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1000, 800);
    setLocationRelativeTo(null);
  }

  @Override
  public void setController(IGUIController controller) throws Exception {
    this.controller = controller;
    initComponents();
  }

  private void initComponents() throws Exception {
    JPanel topPanel = new JPanel(new BorderLayout());

    CalendarSelectionPanel selectionPanel = new CalendarSelectionPanel(controller,
        refreshMonthViewRunnable);
    topPanel.add(selectionPanel, BorderLayout.WEST);

    JPanel navigationPanel = new JPanel(new FlowLayout());
    JButton prevButton = new JButton("<");
    JButton nextButton = new JButton(">");
    monthLabel = new JLabel("", SwingConstants.CENTER);
    JButton newEventButton = new JButton("New Event");
    JButton editMultipleButton = new JButton("Edit Multiple Events");
    navigationPanel.add(prevButton);
    navigationPanel.add(monthLabel);
    navigationPanel.add(nextButton);
    navigationPanel.add(newEventButton);
    navigationPanel.add(editMultipleButton);
    topPanel.add(navigationPanel, BorderLayout.CENTER);

    monthViewPanel = new MonthViewPanel(controller);

    ImportExportPanel importExportPanel = new ImportExportPanel(controller, selectionPanel,
        refreshMonthViewRunnable);

    JPanel bottomPanel = new JPanel(new BorderLayout());
    calendarInfoLabel = new JLabel();
    updateCalendarInfo();
    bottomPanel.add(calendarInfoLabel, BorderLayout.WEST);
    bottomPanel.add(importExportPanel, BorderLayout.EAST);

    setLayout(new BorderLayout());
    add(topPanel, BorderLayout.NORTH);
    add(monthViewPanel, BorderLayout.CENTER);
    add(bottomPanel, BorderLayout.SOUTH);

    prevButton.addActionListener(e -> {
      monthViewPanel.previousMonth();
      updateMonthLabel();
    });
    nextButton.addActionListener(e -> {
      monthViewPanel.nextMonth();
      updateMonthLabel();
    });

    newEventButton.addActionListener(e -> {
      EventCreationDialog dialog = new EventCreationDialog(this, controller);
      dialog.setVisible(true);

      SwingUtilities.invokeLater(refreshMonthViewRunnable);
    });

    editMultipleButton.addActionListener(e -> {
      EditMultipleDialog dialog = new EditMultipleDialog(this, controller);
      dialog.setVisible(true);

      SwingUtilities.invokeLater(refreshMonthViewRunnable);
    });

    updateMonthLabel();
  }

  private void updateMonthLabel() {
    YearMonth ym = monthViewPanel.getCurrentYearMonth();
    monthLabel.setText(ym.getMonth().toString() + " " + ym.getYear());
  }

  /**
   * To show the Current Calendar Name and Zone in the bottom panel west.
   * @throws Exception if there's any exception
   */
  public void updateCalendarInfo() throws Exception {

    String[] info = controller.getCurrentCalendarNameAndZone();
    if (info != null && info.length == 2) {
      String name = info[0];
      String timezone = info[1];
      calendarInfoLabel.setText("Calendar: " + name + " | TimeZone: " + timezone);
    } else {
      calendarInfoLabel.setText("");
    }
  }


  @Override
  public void start() {
    SwingUtilities.invokeLater(() -> setVisible(true));
  }

}
