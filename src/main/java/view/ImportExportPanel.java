package view;

import controller.IGUIController;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * A representation of the ImportExportPanel which has Import and Export Buttons.
 */
public class ImportExportPanel extends JPanel {

  private IGUIController controller;
  private CalendarSelectionPanel selectionPanel;
  private Runnable refreshRunnable;

  /**
   * Constructor for the class.
   *
   * @param controller      injected controller
   * @param selectionPanel  for accessing active calendar info
   * @param refreshRunnable callback to refresh the month view after operations
   */
  public ImportExportPanel(IGUIController controller, CalendarSelectionPanel selectionPanel,
      Runnable refreshRunnable) {
    this.controller = controller;
    this.selectionPanel = selectionPanel;
    this.refreshRunnable = refreshRunnable;
    initComponents();
  }

  private void initComponents() {
    setLayout(new FlowLayout(FlowLayout.RIGHT));
    JButton importButton = new JButton("Import");
    JButton exportButton = new JButton("Export");
    add(importButton);
    add(exportButton);

    importButton.addActionListener(e -> {
      JFileChooser chooser = new JFileChooser();
      int returnVal = chooser.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        String filePath = chooser.getSelectedFile().getAbsolutePath();

        String result = controller.importCalendar(filePath);
        JOptionPane.showMessageDialog(this, result);
        SwingUtilities.invokeLater(refreshRunnable);
      }
    });

    exportButton.addActionListener(e -> {

      showExportDialog();
    });
  }

  /**
   * A small method that prompts the user for a file name and a format, then calls the controller's
   * exportCalendar(...).
   */
  private void showExportDialog() {

    String defaultName = selectionPanel.getActiveCalendarName();
    if (defaultName == null || defaultName.trim().isEmpty()) {
      defaultName = "DefaultCalendar";
    }

    String fileName = JOptionPane.showInputDialog(this, "Enter export file name (path):",
        defaultName);
    if (fileName == null || fileName.trim().isEmpty()) {
      return;
    }

    String format = JOptionPane.showInputDialog(this, "Enter export format (e.g. 'csv'):", "csv");
    if (format == null || format.trim().isEmpty()) {
      return;
    }

    fileName += "." + format;

    String result = controller.exportCalendar(fileName, format);
    JOptionPane.showMessageDialog(this, result);
  }
}
