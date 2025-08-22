package view;

import controller.IGUIController;
import java.awt.FlowLayout;
import java.time.ZoneId;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Panel for Calendar Selection, Edition, and Creation.
 */
public class CalendarSelectionPanel extends JPanel {


  private final Runnable refreshMonthViewRunnable;
  private JComboBox<String> calendarComboBox;

  private IGUIController controller;
  private String activeCalendarName = "Default";

  /**
   * Constructs a new panel for calendar selection and modification.
   *
   * @param controller the GUI controller used to communicate with the backend
   * @param refreshMonthViewRunnable the action to trigger month view refresh
   */
  public CalendarSelectionPanel(IGUIController controller, Runnable refreshMonthViewRunnable) {
    this.controller = controller;
    this.refreshMonthViewRunnable = refreshMonthViewRunnable;
    initComponents();
  }

  /**
   * Returns the currently active calendar name.
   *
   * @return the active calendar name
   */
  public String getActiveCalendarName() {
    return activeCalendarName;
  }

  /**
   * Sets the currently active calendar name.
   *
   * @param name the calendar name to set as active
   */
  public void setActiveCalendarName(String name) {
    this.activeCalendarName = name;
  }

  /**
   * Adds a calendar to the dropdown list.
   *
   * @param calendarName the name of the calendar to add
   */
  public void addCalendar(String calendarName) {
    calendarComboBox.addItem(calendarName);
  }

  private void initComponents() {
    setLayout(new FlowLayout(FlowLayout.LEFT));
    JLabel label = new JLabel("Select Calendar:");
    calendarComboBox = new JComboBox<>();

    String defaultCalendar = "Default";
    String timezone = ZoneId.systemDefault().toString();
    controller.createCalendar(defaultCalendar, timezone);
    controller.useCalendar(defaultCalendar);
    setActiveCalendarName(defaultCalendar);

    calendarComboBox.addItem(defaultCalendar);
    JButton createCalendarButton = new JButton("Create Calendar");

    JButton editCalendarButton = new JButton("Edit Calendar");

    add(label);
    add(calendarComboBox);
    add(createCalendarButton);
    add(editCalendarButton);

    calendarComboBox.addActionListener(e -> {
      String selectedCalendar = (String) calendarComboBox.getSelectedItem();
      if (selectedCalendar == null || selectedCalendar.trim().isEmpty()) {
        return;
      }
      String result = controller.useCalendar(selectedCalendar);
      JOptionPane.showMessageDialog(this, result);
      if (!result.toLowerCase().contains("failed") && !result.toLowerCase().contains("error")) {
        setActiveCalendarName(selectedCalendar);

        SwingUtilities.invokeLater(refreshMonthViewRunnable);
      }
    });

    createCalendarButton.addActionListener(e -> {
      CreateCalendarDialog dialog = new CreateCalendarDialog(SwingUtilities.getWindowAncestor(this),
          controller, this);
      dialog.setVisible(true);

      SwingUtilities.invokeLater(refreshMonthViewRunnable);
    });


    editCalendarButton.addActionListener(e -> {

      String calName = getActiveCalendarName();
      if (calName == null || calName.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "No active calendar selected.", "Calendar Error",
            JOptionPane.ERROR_MESSAGE);
        return;
      }
      String property = JOptionPane.showInputDialog(this,
          "Enter property to edit: (name/timezone)");
      if (property == null || property.trim().isEmpty()) {
        return;
      }
      String newVal = JOptionPane.showInputDialog(this, "Enter new value for " + property + ":");
      if (newVal == null || newVal.trim().isEmpty()) {
        return;
      }

      String res = controller.editCalendar(calName, property, newVal);
      JOptionPane.showMessageDialog(this, res);

      if (property.equalsIgnoreCase("name") && !res.toLowerCase().contains("failed")
          && !res.toLowerCase().contains("error")) {


        calendarComboBox.removeItem(calName);
        calendarComboBox.addItem(newVal);
        setActiveCalendarName(newVal);
        calendarComboBox.setSelectedItem(newVal);
      }
      SwingUtilities.invokeLater(refreshMonthViewRunnable);
    });
  }
}
