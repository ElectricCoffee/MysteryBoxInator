package ui;

import catalogue.Catalogue;
import common.GameCategory;
import config.Config;
import kotlin.Pair;

import javax.swing.*;
import java.awt.event.*;

public class GenerateMysteryBoxDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<MysteryBoxDialogOption> sizeCombo;
    private JComboBox<String> typeCombo;
    private MysteryBoxDialogOption sizeOption;
    private GameCategory typeOption;

    public GenerateMysteryBoxDialog(Config config, Catalogue catalogue) {
        setTitle("Choose what mystery box to generate");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setupSizeCombo(config);
        setupTypeCombo();

        buttonOK.addActionListener(e -> onOK());
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

    private void setupSizeCombo(Config config) {
        var mysteryBoxAmountMap = config.component5();

        mysteryBoxAmountMap.forEach((k, v) ->
            sizeCombo.addItem(new MysteryBoxDialogOption(k, v))
        );

        sizeCombo.addActionListener((e) -> {
            @SuppressWarnings("unchecked")
            var src = (JComboBox<MysteryBoxDialogOption>)e.getSource();
            sizeOption = (MysteryBoxDialogOption)src.getSelectedItem();
        });
    }

    private void setupTypeCombo() {
        for (var i : new String[] { "Trick-Taker", "Variety" }) {
            typeCombo.addItem(i);
        }

        typeCombo.addActionListener((e) -> {
            @SuppressWarnings("unchecked")
            var src = (JComboBox<String>)e.getSource(); // god this is ugly... thanks Java
            var selected = (String)src.getSelectedItem();
            assert selected != null;
            if (selected.startsWith("T")) {
                typeOption = GameCategory.TRICK_TAKER;
            } else if (selected.startsWith("V")) {
                typeOption = GameCategory.VARIETY;
            } else {
                typeOption = null;
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void openDialog(Config config, Catalogue catalogue) {
        GenerateMysteryBoxDialog dialog = new GenerateMysteryBoxDialog(config, catalogue);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
