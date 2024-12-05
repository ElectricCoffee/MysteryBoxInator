package ui;

import common.HrBoolean;
import mysteryBox.MysteryBox;
import mysteryBox.MysteryBoxList;

import javax.swing.*;
import java.awt.event.*;

public class EditShippingInfoDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> packedCombo;
    private JTextField nameField;
    private JTextField numberField;
    private MysteryBox newBox;

    public EditShippingInfoDialog(MysteryBox mysteryBox) {
        setTitle("Edit shipping info for " + mysteryBox.getPrefix());
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setupComboBox();
        populateFields(mysteryBox);

        buttonOK.addActionListener(e -> onOK(mysteryBox));

        buttonCancel.addActionListener(e -> onCancel(mysteryBox));

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel(mysteryBox);
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
                e -> onCancel(mysteryBox),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void setupComboBox() {
        for (var e : new String[] { "Yes", "No" }) {
            packedCombo.addItem(e);
        }
        packedCombo.setSelectedIndex(1); // no by default
    }

    private void populateFields(MysteryBox mysteryBox) {
        packedCombo.setSelectedIndex(mysteryBox.getPacked() ? 0 : 1);
        nameField.setText(mysteryBox.getCustomerName());
        numberField.setText(mysteryBox.getOrderNumber());
    }

    private void onOK(MysteryBox mysteryBox) {
        var packed = packedCombo.getSelectedIndex() == 0;
        var customerName = nameField.getText();
        var orderNumber = numberField.getText();
        newBox = mysteryBox.updateShippingInfo(packed, customerName, orderNumber);
        dispose();
    }

    private void onCancel(MysteryBox mysteryBox) {
        newBox = mysteryBox;
        dispose();
    }

    public static MysteryBox openDialog(MysteryBox mysteryBox) {
        EditShippingInfoDialog dialog = new EditShippingInfoDialog(mysteryBox);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        return dialog.newBox;
    }
}
