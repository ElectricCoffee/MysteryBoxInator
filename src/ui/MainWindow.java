package ui;

import catalogue.Catalogue;
import config.Config;
import io.Filing;
import ui.menu.MenuBar;
import ui.util.CsvUtils;

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
    private JButton generateMysteriesButton;
    private JButton exportBtn;

    public MainWindow() {
        super("Mystery-Box-Inator");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        try {
            Config config = Filing.createConfig();
            Catalogue catalogue = Filing.readWorkingCopy(config);
            var configDtm = configCatalogueTable(config, catalogue);
            configMysteryBoxTable();
            setJMenuBar(new MenuBar(config, catalogue, configDtm));

            // populate the table with the catalogue's contents
            if (!catalogue.getGamesList().isEmpty()) {
                CsvUtils.populateTable(config, catalogue, configDtm);
            }

        } catch (IOException ioe) {
            showErrorDialog(ioe.getMessage(), "File Error!");
        } catch (Exception e) {
            showErrorDialog(e.getMessage(), "Error!");
        }
    }

    void showErrorDialog(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    DefaultTableModel configCatalogueTable(Config config, Catalogue catalogue) {
        var dtm = new DefaultTableModel(null,
                new Object[]{"Name", "Quantity", "Type", "Rarity", "Url", "Paste-Ups?", "Raw Cost", "Retail Price", "Item Profit", "Total Profit"});

        productTable.setModel(dtm);

        new DropTarget(this, new CsvDropListener(this, config, catalogue, dtm));
        dtm.addTableModelListener((e) -> {
            numberOfGamesLoaded.setText(Integer.toString(catalogue.getCountGames()));
            totalStockLabel.setText(Integer.toString(catalogue.getCountTotalInventory()));
            totalProfitLabel.setText("£" + catalogue.getCatalogueProfit().setScale(2, RoundingMode.HALF_UP));
        });

        return dtm;
    }

    DefaultTableModel configMysteryBoxTable() {
        var dtm = new DefaultTableModel(null,
                new Object[] {"Id", "Items", "Type", "Sell Price", "Budget", "Budget Status"});

        mysteryBoxTable.setModel(dtm);

        return dtm;
    }
}
