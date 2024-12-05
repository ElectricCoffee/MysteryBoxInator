package ui;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.Objects;

public class CsvDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonAppend;
    private JButton buttonOverwrite;
    private JButton buttonCancel;
    private JTextField pathField;
    private JButton buttonOpen;
    private File selectedFile;
    private CsvActionSelected actionSelected;

    public CsvDialog() {
        setTitle("Load CSV File");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOverwrite);
        setLocationRelativeTo(null);

        buttonOpen.addActionListener(e -> onFileOpen());
        buttonAppend.addActionListener(e -> onAppend());
        buttonOverwrite.addActionListener(e -> onOverwrite());
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
                e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onFileOpen() {
        // TODO open file dialog, then once a file was selected, populate the file path.
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = chooser.showOpenDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        selectedFile = chooser.getSelectedFile();
        pathField.setText(selectedFile.getAbsolutePath());
    }

    private void onAppend() {
        // TODO: Use the file path to append data to the catalogue
        actionSelected = CsvActionSelected.APPEND;
        dispose();
    }

    private void onOverwrite() {
        // TODO: Use the file path to replace the catalogue
        actionSelected = CsvActionSelected.OVERWRITE;
        dispose();
    }

    private void onCancel() {
        actionSelected = CsvActionSelected.CANCEL;
        dispose();
    }

    public CsvActionSelected openDialog() {
        this.pack();
        this.setVisible(true);

        return actionSelected;
    }

    public File getSelectedFile() {
        // if the file wasn't loaded through normal means, use the text field instead
        return Objects.requireNonNullElseGet(selectedFile, () -> new File(pathField.getText()));
    }
}
