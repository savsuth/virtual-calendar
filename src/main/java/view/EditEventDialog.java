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
 * A dialog that allows editing a single event.
 */
public class EditEventDialog extends JDialog {

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
   * @param parent the parent window for modality
   * @param controller the controller to handle the update operation
   */
  public EditEventDialog(Window parent, IGUIController controller) {
    super(parent, "Edit Single Event", ModalityType.APPLICATION_MODAL);
    this.controller = controller;
    initComponents();
  }

  private void initComponents() {
    setLayout(new BorderLayout(10, 10));
    JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));


    formPanel.add(new JLabel("Existing Subject (for identification):"));
    subjectField = new JTextField();
    formPanel.add(subjectField);

    formPanel.add(new JLabel("Existing Start (YYYY-MM-DDTHH:MM) for identification:"));
    fromField = new JTextField();
    formPanel.add(fromField);


    formPanel.add(new JLabel("Select Property to Edit:"));

    JPanel radioPanel = new JPanel(new GridLayout(3, 2));
    subjectRadio = new JRadioButton("Subject");
    startRadio = new JRadioButton("Start");
    endRadio = new JRadioButton("End");
    descRadio = new JRadioButton("Description");
    locRadio = new JRadioButton("Location");
    JRadioButton publicRadio = new JRadioButton("Public");

    ButtonGroup group = new ButtonGroup();
    List<JRadioButton> radios = Arrays.asList(subjectRadio, startRadio, endRadio, descRadio,
        locRadio, publicRadio);
    for (JRadioButton rb : radios) {
      group.add(rb);
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

    updateButton.addActionListener(e -> updateEvent());
    cancelButton.addActionListener(e -> dispose());

    pack();
    setLocationRelativeTo(getParent());
  }

  private void updateEvent() {


    String existingSubject = subjectField.getText().trim();
    String existingStart = fromField.getText().trim();

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


    String editMode = "SINGLE";


    String result = controller.editEvent(existingSubject, existingStart, property, newValue,
        editMode);

    JOptionPane.showMessageDialog(this, result);
    dispose();
  }
}
