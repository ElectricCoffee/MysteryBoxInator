package ui;

import catalogue.Catalogue;
import config.Config;
import io.Filing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import static config.ConfigKt.*;

public class MainWindow extends JFrame {
    private JTabbedPane tabPane;
    private JTable productTable;
    private JPanel mainPanel;
    private JButton generateMysteriesButton;
    private JLabel totalProfitLabel;
    private JTable mysteryBoxTable;
    private JPanel catalogueTab;
    private JPanel mysteryBoxTab;
    private JButton exportBtn;
    private JLabel numberOfGamesLoaded;
    private JLabel totalStockLabel;

    public MainWindow() {
        super("Mystery-Box-Inator");

        try {
            setContentPane(mainPanel);
            Config config = Filing.Companion.createConfig();
            Catalogue catalogue = new Catalogue(config); // for now, replace with loaded catalogue later.
            var dtm = configTable();
            setJMenuBar(new MenuBar(config, catalogue, dtm));
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1200, 800);
            setLocationRelativeTo(null);
            productTable.setModel(dtm);

            new DropTarget(this, new CsvDropListener(this, catalogue, dtm));
            dtm.addTableModelListener((e) -> {
                numberOfGamesLoaded.setText(Integer.toString(catalogue.getCountGames()));
                totalStockLabel.setText(Integer.toString(catalogue.getCountTotalInventory()));
                totalProfitLabel.setText("£" + catalogue.getCatalogueProfit().setScale(2, RoundingMode.HALF_UP));
            });
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this, ioe.getMessage(), "File Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    DefaultTableModel configTable() {
        return new DefaultTableModel(null, new Object[]{"Name", "Quantity", "Type", "Rarity", "Url", "Paste-Ups?", "Raw Cost", "Retail Price", "Item Profit", "Total Profit"});
    }
}
