package view;

import controller.IGUIController;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * A dialog for event creation.
 */
public class EventCreationDialog extends JDialog {

  private JTextField subjectField;
  private JTextField startField;
  private JTextField endField;
  private JTextField descriptionField;
  private JTextField locationField;
  private JCheckBox publicCheckBox;
  private JCheckBox recurringCheckBox;
  private JPanel recurringPanel;
  private JTextField recurrenceDaysField;
  private JRadioButton occurrencesRadio;
  private JTextField occurrencesField;
  private JTextField untilField;
  private IGUIController controller;

  /**
   * Constructor for the class.
   * @param parent parent window
   * @param controller  injected controller
   */
  public EventCreationDialog(Window parent, IGUIController controller) {
    super(parent, "Create New Event", ModalityType.APPLICATION_MODAL);
    this.controller = controller;
    initComponents();
  }

  private void initComponents() {
    setLayout(new BorderLayout());
    JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));

    formPanel.add(new JLabel("Subject:"));
    subjectField = new JTextField();
    formPanel.add(subjectField);

    formPanel.add(new JLabel("Start (yyyy-MM-ddTHH:MM):"));
    startField = new JTextField();
    formPanel.add(startField);

    formPanel.add(new JLabel("End (yyyy-MM-ddTHH:MM):"));
    endField = new JTextField();
    formPanel.add(endField);

    formPanel.add(new JLabel("Description:"));
    descriptionField = new JTextField();
    formPanel.add(descriptionField);

    formPanel.add(new JLabel("Location:"));
    locationField = new JTextField();
    formPanel.add(locationField);

    formPanel.add(new JLabel("Public?"));
    publicCheckBox = new JCheckBox();
    publicCheckBox.setSelected(true);
    formPanel.add(publicCheckBox);

    recurringCheckBox = new JCheckBox("Recurring Event");
    formPanel.add(recurringCheckBox);
    formPanel.add(new JLabel(""));


    recurringPanel = new JPanel(new GridLayout(0, 2, 5, 5));
    recurringPanel.setBorder(BorderFactory.createTitledBorder("Recurring Options"));
    recurringPanel.add(new JLabel("Recurrence Days (e.g. MTWRF):"));
    recurrenceDaysField = new JTextField();
    recurringPanel.add(recurrenceDaysField);

    occurrencesRadio = new JRadioButton("For Occurrences:");
    JRadioButton untilRadio = new JRadioButton("Until End Date:");
    ButtonGroup grp = new ButtonGroup();
    grp.add(occurrencesRadio);
    grp.add(untilRadio);
    occurrencesRadio.setSelected(true);

    recurringPanel.add(occurrencesRadio);
    occurrencesField = new JTextField();
    recurringPanel.add(occurrencesField);

    recurringPanel.add(untilRadio);
    untilField = new JTextField();
    recurringPanel.add(untilField);

    recurringPanel.setVisible(false);

    recurringCheckBox.addActionListener(e -> {
      recurringPanel.setVisible(recurringCheckBox.isSelected());
      pack();
    });

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(formPanel, BorderLayout.NORTH);
    mainPanel.add(recurringPanel, BorderLayout.CENTER);
    add(mainPanel, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    JButton createButton = new JButton("Create");
    JButton cancelButton = new JButton("Cancel");
    buttonPanel.add(createButton);
    buttonPanel.add(cancelButton);
    add(buttonPanel, BorderLayout.SOUTH);

    createButton.addActionListener(e -> onCreate());
    cancelButton.addActionListener(e -> dispose());

    pack();
    setLocationRelativeTo(getParent());
  }

  private void onCreate() {

    String subject = subjectField.getText().trim();
    String start = startField.getText().trim();
    String end = endField.getText().trim();
    String description = descriptionField.getText().trim();
    String location = locationField.getText().trim();
    boolean isPublic = publicCheckBox.isSelected();


    if (subject.isEmpty() || start.isEmpty() || end.isEmpty()) {

      JOptionPane.showMessageDialog(this,
          "Subject, Start, and End are recommended fields.",
          "Input Notice",
          JOptionPane.WARNING_MESSAGE);

    }

    String result;
    boolean autoDecline = false;
    if (!recurringCheckBox.isSelected()) {

      result = controller.createSingleEvent(subject, start, end, description, location, isPublic,
          autoDecline);
    } else {
      String recDays = recurrenceDaysField.getText().trim();
      String occCountStr = "";
      String recEndDateStr = "";
      if (occurrencesRadio.isSelected()) {
        occCountStr = occurrencesField.getText().trim();
      } else {
        recEndDateStr = untilField.getText().trim();
      }
      result = controller.createRecurringEvent(subject, start, end,
          description, location, isPublic, recDays, occCountStr, recEndDateStr, autoDecline);
    }

    JOptionPane.showMessageDialog(this, result);
    dispose();
  }
}
