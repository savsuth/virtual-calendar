package view;

import controller.IGUIController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Represents a single day cell in the calendar's month view.
 */
public class DayPanel extends JPanel {

  private final int day;
  private final YearMonth yearMonth;
  private final IGUIController controller;
  private JTextArea eventSummary;

  /**
   * Constructor for the class.
   *
   * @param day        the day of the month
   * @param yearMonth  the year and month this panel belongs to
   * @param controller the controller used to interact with calendar data
   */
  public DayPanel(int day, YearMonth yearMonth, IGUIController controller) {
    this.day = day;
    this.yearMonth = yearMonth;
    this.controller = controller;
    initComponents();
    refreshEventSummary();
  }

  private void initComponents() {
    setBorder(BorderFactory.createLineBorder(Color.GRAY));
    setLayout(new BorderLayout());

    JLabel dayLabel = new JLabel(String.valueOf(day), SwingConstants.RIGHT);
    add(dayLabel, BorderLayout.NORTH);

    eventSummary = new JTextArea();
    eventSummary.setEditable(false);
    eventSummary.setFont(new Font("SansSerif", Font.PLAIN, 10));
    eventSummary.setOpaque(false);
    eventSummary.setLineWrap(true);
    eventSummary.setWrapStyleWord(true);
    add(eventSummary, BorderLayout.CENTER);

    MouseAdapter adapter = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
          showDayEvents();
        } else if (SwingUtilities.isRightMouseButton(e)) {
          showEditPopup(e);
        }
      }
    };
    addMouseListener(adapter);
    dayLabel.addMouseListener(adapter);
    eventSummary.addMouseListener(adapter);
  }

  /**
   * Refreshes the summary text to display the latest events for the represented day.
   */
  public void refreshEventSummary() {
    LocalDate date = yearMonth.atDay(day);

    String output = controller.printEventsOn(date);

    if (output == null || output.trim().isEmpty()) {
      eventSummary.setText("No events");
    } else {
      eventSummary.setText(output);
    }
  }

  private void showDayEvents() {
    LocalDate date = yearMonth.atDay(day);
    String events = controller.printEventsOn(date);
    if (events == null || events.trim().isEmpty()) {
      events = "No events";
    }
    JOptionPane.showMessageDialog(DayPanel.this, events, "Events on " + date,
        JOptionPane.INFORMATION_MESSAGE);
  }

  private void showEditPopup(MouseEvent e) {

    String currentText = eventSummary.getText();
    if (currentText == null || currentText.trim().equalsIgnoreCase("No events")) {
      return;
    }


    JPopupMenu popup = new JPopupMenu();
    JMenuItem editItem = new JMenuItem("Edit Event");
    editItem.addActionListener(ae -> openEditDialog());
    popup.add(editItem);
    popup.show(this, e.getX(), e.getY());
  }


  private void openEditDialog() {


    EditEventDialog dialog = new EditEventDialog(
        SwingUtilities.getWindowAncestor(this),
        controller
    );
    dialog.setVisible(true);
    refreshEventSummary();
  }
}
