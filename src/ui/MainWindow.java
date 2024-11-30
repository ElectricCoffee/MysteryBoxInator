package ui;

import catalogue.Catalogue;
import config.Config;
import io.Filing;
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

        try {
            Config config = Filing.createConfig();
            Catalogue catalogue = Filing.readWorkingCopy(config);
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

            editButton.addActionListener((e) -> EditItemDialog.openDialog(null)); // TODO: replace null with actual code
            generateMysteriesButton.addActionListener((e) -> GenerateMysteryBoxDialog.openDialog(config, catalogue));

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

        return dtm;
    }

    DefaultTableModel configMysteryBoxTable() {
        var dtm = new DefaultTableModel(null,
                new Object[] {"Id", "Items", "Type", "Sell Price", "Budget", "Budget Status"});

        dtm.addTableModelListener((e) -> {}); // to be filled

        return dtm;
    }
}
