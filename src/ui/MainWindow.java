package ui;

import catalogue.Catalogue;
import config.Config;
import io.Filing;
import mysteryBox.MysteryBoxList;
import mysteryBox.assembler.MysteryBoxAssemblerFactory;
import ui.listeners.CatalogueEditButtonActionListener;
import ui.listeners.CsvDropListener;
import ui.listeners.MysteryBoxGenerateButtonListener;
import ui.menu.MenuBar;
import ui.util.TableUtils;
import ui.util.ErrorDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.dnd.*;
import java.io.IOException;
import java.math.RoundingMode;

public class MainWindow extends JFrame {
    private JTable productTable;
    private JPanel mainPanel;
    private JLabel totalProfitLabel;
    private JTable mysteryBoxTable;
    private JLabel numberOfGamesLoaded;
    private JLabel totalStockLabel;
    private JTabbedPane tabPane;
    private JPanel catalogueTab;
    private JPanel mysteryBoxTab;
    private JButton editButton;
    private JButton exportBtn;
    private JButton generateMysteriesButton;

    public MainWindow() {
        super("Mystery-Box-Inator");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        editButton.setEnabled(false); // have it be grayed out at first

        try {
            Config config = Filing.createConfig();
            Catalogue catalogue = Filing.readWorkingCopy(config);
            MysteryBoxList mysteryBoxList = new MysteryBoxList(); // TODO: replace with reading the working copy
            var catalogueDtm = configCatalogueTable(config, catalogue);
            var mysteryDtm = configMysteryBoxTable();

            configMysteryBoxTable();
            setJMenuBar(new MenuBar(config, catalogue, catalogueDtm));

            productTable.setModel(catalogueDtm);
            mysteryBoxTable.setModel(mysteryDtm);

            // populate the table with the catalogue's contents
            if (!catalogue.getGamesList().isEmpty()) {
                TableUtils.populateCatalogueTable(config, catalogue, catalogueDtm);
            }

            editButton.addActionListener(new CatalogueEditButtonActionListener(config, catalogue, productTable));
            generateMysteriesButton.addActionListener(new MysteryBoxGenerateButtonListener(config, catalogue, mysteryBoxList, catalogueDtm, mysteryDtm));

        } catch (IOException ioe) {
            new ErrorDialog(this).open(ioe.getMessage(), "File Error!");
        } catch (Exception e) {
            new ErrorDialog(this).open(e.getMessage(), "Error!");
        }
    }

    DefaultTableModel configCatalogueTable(Config config, Catalogue catalogue) {
        var dtm = new DefaultTableModel(null,
                new Object[]{"Name", "Quantity", "Type", "Rarity", "BGG ID", "Paste-Ups?", "Raw Cost", "Retail Price", "Item Profit", "Total Profit"});

        new DropTarget(this, new CsvDropListener(this, config, catalogue, dtm));
        dtm.addTableModelListener((e) -> {
            numberOfGamesLoaded.setText(Integer.toString(catalogue.getCountGames()));
            totalStockLabel.setText(Integer.toString(catalogue.getCountTotalInventory()));
            totalProfitLabel.setText("£" + catalogue.getCatalogueProfit().setScale(2, RoundingMode.HALF_UP));
        });

        productTable.getSelectionModel().addListSelectionListener((e) -> {
            editButton.setEnabled(true);
        });

        return dtm;
    }

    DefaultTableModel configMysteryBoxTable() {
        var dtm = new DefaultTableModel(null,
                new Object[] {"Id", "Units", "Items", "Type", "Total Item Value", "Box Price", "Price Deviance", "Sold?"});

        dtm.addTableModelListener((e) -> {}); // to be filled

        return dtm;
    }
}
