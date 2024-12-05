package ui;

import ui.util.ErrorDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InfoDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel description;
    private JLabel logos;

    public InfoDialog() {
        setTitle("About");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setResizable(false);
        setLocationRelativeTo(null);

        buttonOK.addActionListener(e -> onOK());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onOK();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onOK(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        description.setText("<html><p>" +
                "This software was made by City Hare Studio for and in collaboration with Travel-Games.co.uk and is provided as-is.<br><br>" +
                "City Hare Studio is a small independent board game design studio based out of Aalborg, Denmark.<br>" +
                "Travel-Games.co.uk is a small independent board game store based out of London, England.<br><br>" +
                "The logos for City Hare Studio and Travel-Games.co.uk belong to the individual parties.<br>" +
                "Copyright ©2024 City Hare Studio & Travel-Games.co.uk" +
                "</p></html>");

        description.setPreferredSize(new Dimension(600, -1));

//        var path = this.getClass().getResource("resources/logos.png");

//        logos.setIcon(new ImageIcon(new ImageIcon("resources/logos.png").getImage().getScaledInstance(300, 169, Image.SCALE_SMOOTH)));

        try {
            var resource = getClass().getResource("/resources/logos.png");
            if (resource == null) throw new Exception("Could not find image");
            var image = new ImageIcon(resource).getImage();
            var scaledImage = image.getScaledInstance(300, 169, Image.SCALE_SMOOTH);
            logos.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            new ErrorDialog(this).open(e.getMessage(), "Couldn't load image");
        }
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    public static void openDialog() {
        InfoDialog dialog = new InfoDialog();
        dialog.pack();
        dialog.setVisible(true);
    }
}
