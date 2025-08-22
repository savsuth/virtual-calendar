package view;

import controller.IGUIController;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Generates a simple Create Calendar Dialog.
 */
public class CreateCalendarDialog extends JDialog {

  private JTextField nameField;
  private JTextField timezoneField;
  private IGUIController controller;

  private CalendarSelectionPanel selectionPanel;

  /**
   * Constructs a CreateCalendarDialog.
   *
   * @param parent         the parent window
   * @param controller     the injected controller
   * @param selectionPanel a reference to the calendar selection panel for updating calendar names
   *
   */
  public CreateCalendarDialog(Window parent, IGUIController controller,
      CalendarSelectionPanel selectionPanel) {
    super(parent, "Create New Calendar", ModalityType.APPLICATION_MODAL);
    this.controller = controller;
    this.selectionPanel = selectionPanel;
    initComponents();
  }

  private void initComponents() {
    setLayout(new BorderLayout());
    JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));

    formPanel.add(new JLabel("Calendar Name:"));
    nameField = new JTextField();
    formPanel.add(nameField);

    formPanel.add(new JLabel("Timezone (e.g., America/New_York):"));
    timezoneField = new JTextField();
    formPanel.add(timezoneField);

    add(formPanel, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    JButton createButton = new JButton("Create");
    JButton cancelButton = new JButton("Cancel");
    buttonPanel.add(createButton);
    buttonPanel.add(cancelButton);
    add(buttonPanel, BorderLayout.SOUTH);

    createButton.addActionListener(e -> {
      String calendarName = nameField.getText().trim();
      String timezone = timezoneField.getText().trim();
      if (calendarName.isEmpty() || timezone.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Both fields are required.", "Input Error",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

      String result = controller.createCalendar(calendarName, timezone);
      JOptionPane.showMessageDialog(this, result);

      if (!result.toLowerCase().contains("failed") && !result.toLowerCase().contains("invalid")) {
        if (selectionPanel != null) {
          selectionPanel.addCalendar(calendarName);
        }
        dispose();
      }
    });

    cancelButton.addActionListener(e -> dispose());

    pack();
    setLocationRelativeTo(getParent());
  }
}
