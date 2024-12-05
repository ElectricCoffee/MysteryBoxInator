package ui;

import catalogue.Catalogue;
import config.Config;
import errors.ConfigMissingException;
import io.Filing;
import mysteryBox.MysteryBoxList;
import ui.listeners.*;
import ui.menu.MenuBar;
import ui.models.CatalogueTableModel;
import ui.models.MysteryBoxTableModel;
import ui.util.TableUtils;
import ui.util.ErrorDialog;
import util.NumUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.dnd.*;
import java.io.IOException;

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
    private JButton editShippingInfoButton;
    private JButton generateMysteriesButton;
    private JButton viewBoxButton;

    public MainWindow() {
        super("Mystery-Box-Inator");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        editButton.setEnabled(false); // have it be grayed out at first
        viewBoxButton.setEnabled(false);
        editShippingInfoButton.setEnabled(false);

        try {
            Config config = Filing.createConfig();
            Catalogue catalogue = Filing.readWorkingCopy(config);
            MysteryBoxList mysteryBoxList = Filing.readMysteryBoxWorkingCopy(config);
            var catalogueDtm = configCatalogueTable(config, catalogue);
            var mysteryDtm = configMysteryBoxTable();

            setJMenuBar(new MenuBar(config, catalogue, mysteryBoxList, catalogueDtm, mysteryDtm));

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
            viewBoxButton.addActionListener(new ViewMysteryBoxButtonListener(mysteryBoxList, mysteryBoxTable));
            editShippingInfoButton.addActionListener(new EditShippingInfoListener(config, mysteryBoxList, mysteryBoxTable));
        } catch (IOException ioe) {
            new ErrorDialog(this).open(ioe.getMessage(), "File Error!");
        } catch (ConfigMissingException cme) {
            new ErrorDialog(this).open(cme.getLongMessage(), cme.getTitle());
            System.exit(1);
        } catch (Exception | Error e) {
            new ErrorDialog(this).open(e.getMessage(), "Something Went Wrong!");
        }
    }

    DefaultTableModel configCatalogueTable(Config config, Catalogue catalogue) {
        var dtm = new CatalogueTableModel();

        catalogueTable.setAutoCreateRowSorter(true);

        new DropTarget(this, new CsvDropListener(this, config, catalogue, dtm));
        dtm.addTableModelListener((e) -> {
            numberOfGamesLoaded.setText(Integer.toString(catalogue.getCountGames()));
            totalStockLabel.setText(Integer.toString(catalogue.getCountTotalInventory()));
            totalProfitLabel.setText(NumUtils.asPrice(catalogue.getCatalogueProfit()));
        });

        catalogueTable.getSelectionModel().addListSelectionListener(e -> {
            var isEmpty = catalogueTable.getSelectionModel().isSelectionEmpty();
            editButton.setEnabled(!isEmpty);
        });

        return dtm;
    }

    DefaultTableModel configMysteryBoxTable() {
        var dtm = new MysteryBoxTableModel();

        dtm.addTableModelListener((e) -> {}); // to be filled
        mysteryBoxTable.getSelectionModel().addListSelectionListener(e -> {
            var isEmpty = mysteryBoxTable.getSelectionModel().isSelectionEmpty();
            viewBoxButton.setEnabled(!isEmpty);
            editShippingInfoButton.setEnabled(!isEmpty);
        });

        return dtm;
    }
}
