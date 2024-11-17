package ui;

import catalogue.Catalogue;
import config.Config;

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
    private Config config;
    private Catalogue catalogue;

    public MainWindow() {
        super("Mystery-Box-Inator");

        try {
            setContentPane(mainPanel);
            config = createConfig();
            catalogue = new Catalogue(config); // for now, replace with loaded catalogue later.
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

    Config createConfig() throws IOException {
        var configFolder = Paths.get(configFolderPath);
        var configFile = Paths.get(configFilePath);

        if (!Files.exists(configFolder)) {
            Files.createDirectory(configFolder);
        }

        if (!Files.exists(configFile)) {
            Files.createFile(configFile);

            Files.write(configFile, defaultConfigString.getBytes());
        }

        return Config.Companion.fromFile(configFile);
    }

    void backupCatalogue() throws IOException {
        var outputDir = Paths.get(config.getIo().getOutputDirectory());

        var date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        var outputFile = Paths.get(outputDir + File.separator + "catalogue-" + date + ".backup.csv");

        if (!Files.exists(outputDir)) {
            Files.createDirectory(outputDir);
        }

        // writes a timestamped backup of the current catalogue
        Files.write(outputFile, catalogue.toCsv(), StandardOpenOption.CREATE);
    }

    DefaultTableModel configTable() {
        return new DefaultTableModel(null, new Object[]{"Name", "Quantity", "Type", "Rarity", "Url", "Paste-Ups?", "Raw Cost", "Retail Price", "Item Profit", "Total Profit"});
    }
}
