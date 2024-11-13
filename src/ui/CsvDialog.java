package ui;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class CsvDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonAppend;
    private JButton buttonOverwrite;
    private JButton buttonCancel;
    private JTextField pathField;
    private JButton buttonOpen;

    public CsvDialog() {
        setTitle("Load CSV File");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOverwrite);
        setLocationRelativeTo(null);

        buttonOpen.addActionListener(e -> onOpen());
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

    private void onOpen() {
        // TODO open file dialog, then once a file was selected, populate the file path.
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = chooser.showOpenDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        File selectedFile = chooser.getSelectedFile();
        pathField.setText(selectedFile.getAbsolutePath());
        System.out.println("Selected file: " + selectedFile.getAbsolutePath());

    }

    private void onAppend() {
        // TODO: Use the file path to append data to the catalogue
        dispose();
    }

    private void onOverwrite() {
        // TODO: Use the file path to replace the catalogue
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public void openDialog() {
        this.pack();
        this.setVisible(true);
    }
}
