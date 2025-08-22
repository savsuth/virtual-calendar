package view;

import controller.IGUIController;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.Arrays;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * A dialog that allows multiple editing of the events. Particularly {@code FROM & ALL Mode}.
 */
public class EditMultipleDialog extends JDialog {

  private JTextField subjectField;
  private JTextField fromField;
  private JRadioButton subjectRadio;
  private JRadioButton startRadio;
  private JRadioButton endRadio;
  private JRadioButton descRadio;
  private JRadioButton locRadio;
  private JTextField newValueField;
  private IGUIController controller;

  /**
   * Constructor for the class.
   *
   * @param parent parent window
   * @param controller  injected controller
   */
  public EditMultipleDialog(Window parent, IGUIController controller) {
    super(parent, "Edit Multiple Events", ModalityType.APPLICATION_MODAL);
    this.controller = controller;
    initComponents();
  }


  private void initComponents() {
    setLayout(new BorderLayout(10, 10));

    JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));

    formPanel.add(new JLabel("Subject (identifies which events to edit):"));
    subjectField = new JTextField();
    formPanel.add(subjectField);

    formPanel.add(new JLabel("'From' time (YYYY-MM-DDTHH:MM). Leave blank for ALL:"));
    fromField = new JTextField();
    formPanel.add(fromField);

    formPanel.add(new JLabel("Property to edit:"));
    JPanel radioPanel = new JPanel(new GridLayout(3, 2));
    subjectRadio = new JRadioButton("Subject");
    startRadio = new JRadioButton("Start");
    endRadio = new JRadioButton("End");
    descRadio = new JRadioButton("Description");
    locRadio = new JRadioButton("Location");
    JRadioButton publicRadio = new JRadioButton("Public");

    ButtonGroup grp = new ButtonGroup();
    List<JRadioButton> props = Arrays.asList(subjectRadio, startRadio, endRadio, descRadio,
        locRadio, publicRadio);
    for (JRadioButton rb : props) {
      grp.add(rb);
      radioPanel.add(rb);
    }
    subjectRadio.setSelected(true);
    formPanel.add(radioPanel);

    formPanel.add(new JLabel("New Value:"));
    newValueField = new JTextField();
    formPanel.add(newValueField);

    add(formPanel, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    JButton updateButton = new JButton("Update");
    JButton cancelButton = new JButton("Cancel");
    buttonPanel.add(updateButton);
    buttonPanel.add(cancelButton);
    add(buttonPanel, BorderLayout.SOUTH);

    updateButton.addActionListener(e -> onUpdate());
    cancelButton.addActionListener(e -> dispose());

    pack();
    setLocationRelativeTo(getParent());
  }

  private void onUpdate() {

    String subject = subjectField.getText().trim();
    String fromStr = fromField.getText().trim();

    String property;
    if (subjectRadio.isSelected()) {
      property = "subject";
    } else if (startRadio.isSelected()) {
      property = "start";
    } else if (endRadio.isSelected()) {
      property = "end";
    } else if (descRadio.isSelected()) {
      property = "description";
    } else if (locRadio.isSelected()) {
      property = "location";
    } else {
      property = "public";
    }

    String newValue = newValueField.getText().trim();

    String mode = fromStr.isEmpty() ? "ALL" : "FROM";

    String result = controller.editEvent(subject, fromStr, property, newValue, mode);
    JOptionPane.showMessageDialog(this, result);
    dispose();
  }
}
