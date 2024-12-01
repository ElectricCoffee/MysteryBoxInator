package ui;

import catalogue.CatalogueEntry;
import common.GameCategory;
import common.GameRarity;
import game.Game;
import ui.util.ErrorDialog;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class EditItemDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField titleField;
    private JSpinner quantitySpinner;
    private JComboBox<GameCategory> typeCombo;
    private JComboBox<GameRarity> rarityCombo;
    private JTextField urlField;
    private JComboBox<String> pasteUpsCombo;
    private JTextField rawCostField;
    private JTextField retailPriceField;
    private CatalogueEntry newEntry;

    public EditItemDialog(CatalogueEntry catalogueEntry) {
        if (catalogueEntry != null) {
            setTitle("Edit Game/Accessory");
        } else {
            setTitle("Add Game/Accessory");
        }
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setupQuantitySpinner();
        setupTypeCombo();
        setupRarityCombo();
        setupPasteUpsCombo();
        setupRawCostField();
        setupRetailPriceField();

        populateFields(catalogueEntry);

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

    private void populateFields(CatalogueEntry catalogueEntry) {
        if (catalogueEntry == null) {
            var game = catalogueEntry.getGame();

            quantitySpinner.setValue(0);
            typeCombo.setSelectedIndex(0);
            rarityCombo.setSelectedIndex(0);
            pasteUpsCombo.setSelectedIndex(1); // make "no" the default
            rawCostField.setText("0.00");
            retailPriceField.setText("0.00");
            return;
        }
        var game = catalogueEntry.getGame();

        titleField.setText(catalogueEntry.getTitle());
        quantitySpinner.setValue(catalogueEntry.getQuantity());
        typeCombo.setSelectedItem(game.getGameCategory());
        rarityCombo.setSelectedItem(game.getRarity());
        urlField.setText(game.getSafeBggUrl());
        pasteUpsCombo.setSelectedIndex(game.getRequiresPasteUps() ? 0 : 1);
        rawCostField.setText(game.getImportCost().setScale(2, RoundingMode.HALF_UP).toString());
        retailPriceField.setText(game.getRetailValue().setScale(2, RoundingMode.HALF_UP).toString());
    }

    private void setupQuantitySpinner() {
        var editor = (JSpinner.DefaultEditor)quantitySpinner.getEditor();
        editor.getTextField().setHorizontalAlignment(SwingConstants.LEFT);
    }

    private void setupTypeCombo() {
        GameCategory.getEntries().forEach((e) -> {
            typeCombo.addItem(e);
        });
    }

    private void setupRarityCombo() {
        GameRarity.getEntries().forEach(e -> {
            rarityCombo.addItem(e);
        });
    }

    private void setupPasteUpsCombo() {
        for (var e : new String[] { "Yes", "No" }) {
            pasteUpsCombo.addItem(e);
        }
    }

    private void setupRawCostField() {
        var doc = (AbstractDocument)rawCostField.getDocument();
        doc.setDocumentFilter(new DecimalDocumentFilter());
    }

    private void setupRetailPriceField() {
        var doc = (AbstractDocument)retailPriceField.getDocument();
        doc.setDocumentFilter(new DecimalDocumentFilter());
    }

    private void onOK() {
        // add your code here
        try {
            URL url;
            if (urlField.getText().isEmpty()) {
                url = null;
            } else {
                url = new URL(urlField.getText());
            }

            var game = new Game(
                    titleField.getText(),
                    (GameCategory) Objects.requireNonNull(typeCombo.getSelectedItem()),
                    (GameRarity) Objects.requireNonNull(rarityCombo.getSelectedItem()),
                    url,
                    pasteUpsCombo.getSelectedIndex() == 0,
                    new BigDecimal(rawCostField.getText()),
                    new BigDecimal(retailPriceField.getText())
            );
            newEntry = new CatalogueEntry(game, (Integer) quantitySpinner.getValue());
            dispose();
        } catch (MalformedURLException mue) {
            new ErrorDialog(this).open(mue.getMessage(), "Invalid URL");
        } catch (NullPointerException npe) {
            new ErrorDialog(this).open(npe.getMessage(), "Invalid Combo Box");
        }
    }

    private void onCancel() {
        newEntry = null;
        dispose();
    }

    public static CatalogueEntry openDialog(CatalogueEntry c) {
        EditItemDialog dialog = new EditItemDialog(c);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        return dialog.newEntry;
    }
}
