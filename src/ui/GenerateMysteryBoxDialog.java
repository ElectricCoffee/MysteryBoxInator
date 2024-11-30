package ui;

import catalogue.Catalogue;
import common.GameCategory;
import config.Config;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.*;
import java.math.BigDecimal;

public class GenerateMysteryBoxDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<MysteryBoxDialogOption> sizeCombo;
    private JComboBox<String> typeCombo;
    private Pair<BigDecimal, GameCategory> output;

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

        sizeCombo.setSelectedIndex(0);
    }

    private void setupTypeCombo() {
        for (var i : new String[] { "Trick-Taker", "Variety" }) {
            typeCombo.addItem(i);
        }

        typeCombo.setSelectedIndex(0);
    }

    private void onOK() {
        var sizeOption = (MysteryBoxDialogOption)sizeCombo.getSelectedItem();
        var selected = (String)typeCombo.getSelectedItem();
        assert selected != null;
        var typeOption = selected.startsWith("T")
                ? GameCategory.TRICK_TAKER
                : (selected.startsWith("V") ? GameCategory.VARIETY : null);

        assert sizeOption != null;
        output = new Pair<>(sizeOption.getPrice(), typeOption);

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        output = null;
        dispose();
    }

    public static Pair<BigDecimal, GameCategory> openDialog(Config config, Catalogue catalogue) {
        GenerateMysteryBoxDialog dialog = new GenerateMysteryBoxDialog(config, catalogue);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        return dialog.output;
    }
}
