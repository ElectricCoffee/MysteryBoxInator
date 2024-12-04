package ui;

import common.HrPrice;
import mysteryBox.MysteryBox;
import mysteryBox.MysteryBoxList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class ViewMysteryBoxDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTable gamesTable;
    private JLabel quantityLabel;
    private JLabel valueLabel;

    public ViewMysteryBoxDialog(MysteryBox mysteryBox) {
        setTitle("Viewing Mystery Box #" + mysteryBox.getId().substring(0, 5));
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        var dtm = configTable();
        populateTable(dtm, mysteryBox);

        quantityLabel.setText(Integer.toString(mysteryBox.getCount()));
        valueLabel.setText(new HrPrice(mysteryBox.getTotalValue()).toString());

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onOK();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private DefaultTableModel configTable() {
        var dtm = new DefaultTableModel(null, new Object[] {"Title", "Category", "Rarity"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        gamesTable.setModel(dtm);

        return dtm;
    }

    private void populateTable(DefaultTableModel dtm, MysteryBox data) {
        for (var datum : data.getItems()) {
            dtm.addRow(new Object[] { datum.getTitle(), datum.getGameCategory().toHumanReadable(), datum.getRarity().toHumanReadable() });
        }
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    public static void openDialog(MysteryBox mysteryBox) {
        ViewMysteryBoxDialog dialog = new ViewMysteryBoxDialog(mysteryBox);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
