package ui;

import catalogue.Catalogue;
import common.GameRarity;
import config.Config;
import io.Filing;
import mysteryBox.MysteryBoxList;
import ui.listeners.CatalogueEditButtonActionListener;
import ui.listeners.CsvDropListener;
import ui.listeners.MysteryBoxGenerateButtonListener;
import ui.menu.MenuBar;
import ui.models.CatalogueTableModel;
import ui.util.TableUtils;
import ui.util.ErrorDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.dnd.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.Comparator;

public class MainWindow extends JFrame {
    private JTable catalogueTable;
    private JPanel mainPanel;
    private JLabel totalProfitLabel;
    private JTable mysteryBoxTable;
    private JLabel numberOfGamesLoaded;
    private JLabel totalStockLabel;
    private JTabbedPane tabPane;
    private JPanel catalogueTab;
    private JPanel mysteryBoxTab;
    private JButton editButton;
    private JButton soldButton;
    private JButton generateMysteriesButton;
    private JButton deleteBoxButton;

    public MainWindow() {
        super("Mystery-Box-Inator");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        editButton.setEnabled(false); // have it be grayed out at first
        deleteBoxButton.setEnabled(false);
        soldButton.setEnabled(false);

        try {
            Config config = Filing.createConfig();
            Catalogue catalogue = Filing.readWorkingCopy(config);
            MysteryBoxList mysteryBoxList = Filing.readMysteryBoxWorkingCopy(config);
            var catalogueDtm = configCatalogueTable(config, catalogue);
            var mysteryDtm = configMysteryBoxTable();

            setJMenuBar(new MenuBar(config, catalogue, catalogueDtm));

            catalogueTable.setModel(catalogueDtm);
            mysteryBoxTable.setModel(mysteryDtm);

            // populate the table with the catalogue's contents
            if (!catalogue.getGamesList().isEmpty()) {
                TableUtils.populateCatalogueTable(config, catalogue, catalogueDtm);
            }

            if (!mysteryBoxList.getMysteryBoxes().isEmpty()) {
                TableUtils.populateMysteryBoxTable(config, mysteryBoxList, mysteryDtm);
            }

            editButton.addActionListener(new CatalogueEditButtonActionListener(config, catalogue, catalogueTable));
            generateMysteriesButton.addActionListener(new MysteryBoxGenerateButtonListener(config, catalogue, mysteryBoxList, catalogueDtm, mysteryDtm));

        } catch (IOException ioe) {
            new ErrorDialog(this).open(ioe.getMessage(), "File Error!");
        } catch (Exception e) {
            new ErrorDialog(this).open(e.getMessage(), "Error!");
        }
    }

    DefaultTableModel configCatalogueTable(Config config, Catalogue catalogue) {
        var dtm = new CatalogueTableModel();

        catalogueTable.setAutoCreateRowSorter(true);

        new DropTarget(this, new CsvDropListener(this, config, catalogue, dtm));
        dtm.addTableModelListener((e) -> {
            numberOfGamesLoaded.setText(Integer.toString(catalogue.getCountGames()));
            totalStockLabel.setText(Integer.toString(catalogue.getCountTotalInventory()));
            totalProfitLabel.setText("£" + catalogue.getCatalogueProfit().setScale(2, RoundingMode.HALF_UP));
        });

        catalogueTable.getSelectionModel().addListSelectionListener(e -> {
            var isEmpty = catalogueTable.getSelectionModel().isSelectionEmpty();
            editButton.setEnabled(!isEmpty);
        });

        return dtm;
    }

    DefaultTableModel configMysteryBoxTable() {
        var dtm = new DefaultTableModel(null,
                new Object[] {"Id", "Units", "Items", "Type", "Total Item Value", "Box Price", "Price Deviance", "Sold?"});

        dtm.addTableModelListener((e) -> {}); // to be filled
        mysteryBoxTable.getSelectionModel().addListSelectionListener(e -> {
            var isEmpty = mysteryBoxTable.getSelectionModel().isSelectionEmpty();
            deleteBoxButton.setEnabled(!isEmpty);
            soldButton.setEnabled(!isEmpty);
        });

        mysteryBoxTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                var row = mysteryBoxTable.rowAtPoint(e.getPoint());
                if (row == -1) {
                    return;
                }

                var isSold = mysteryBoxTable.getValueAt(row, 7); // 7 should be "Sold?"

                if (isSold.equals("No")) {
                    soldButton.setText("Mark Sold");
                } else {
                    soldButton.setText("Mark Unsold");
                }
            }
        });

        return dtm;
    }
}
