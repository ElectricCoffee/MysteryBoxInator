package ui;

import catalogue.Catalogue;
import common.GameCategory;
import config.Config;

import javax.swing.*;
import java.awt.event.*;
import java.util.Comparator;

public class GenerateMysteryBoxDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<MysteryBoxDialogOption> sizeCombo;
    private JComboBox<String> typeCombo;
    private JLabel gameTypeLabel;
    private JComboBox<String> excludeBox;
    private MysteryBoxDialogResult output;

    public GenerateMysteryBoxDialog(Config config, Catalogue catalogue) {
        setTitle("Choose what mystery box to generate");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setupSizeCombo(config);
        setupTypeCombo();
        setupExcludeCombo();

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

        mysteryBoxAmountMap
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(a -> a.getValue().component1()))
                .forEach((e) ->
            sizeCombo.addItem(new MysteryBoxDialogOption(e.getKey(), e.getValue()))
        );

        sizeCombo.setSelectedIndex(0);
    }

    private void setupTypeCombo() {
        var labels = new String[] { "Trick-Taker", "Variety" };
        for (var i : labels) {
            typeCombo.addItem(i);
        }

        typeCombo.addActionListener((e) -> {
            var src = (JComboBox<String>)e.getSource();
            if (src.getSelectedIndex() == 0) {
                gameTypeLabel.setText("Exclude Variety Game?");
            } else {
                gameTypeLabel.setText("Exclude Trick-Taker?");
            }
        });

        typeCombo.setSelectedIndex(0);
    }

    private void setupExcludeCombo() {
        for (var i : new String[] { "No", "Yes" }) {
            excludeBox.addItem(i);
        }

        excludeBox.setSelectedIndex(0);
    }

    private void onOK() {
        var sizeOption = (MysteryBoxDialogOption)sizeCombo.getSelectedItem();
        var selected = (String)typeCombo.getSelectedItem();
        assert selected != null;
        var typeOption = selected.startsWith("T")
                ? GameCategory.TRICK_TAKER
                : (selected.startsWith("V") ? GameCategory.VARIETY : null);

        var excludeOption = excludeBox.getSelectedIndex() == 1;

        assert sizeOption != null;
        output = new MysteryBoxDialogResult(sizeOption.getPrice(), sizeOption.getShortLabel(), typeOption, excludeOption);

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        output = null;
        dispose();
    }

    public static MysteryBoxDialogResult openDialog(Config config, Catalogue catalogue) {
        GenerateMysteryBoxDialog dialog = new GenerateMysteryBoxDialog(config, catalogue);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        return dialog.output;
    }
}
