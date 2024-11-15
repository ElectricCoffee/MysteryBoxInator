package ui;

import catalogue.Catalogue;
import config.Config;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static config.ConfigKt.*;

public class MainWindow extends JFrame {
    private JTabbedPane tabPane;
    private JTable productTable;
    private JPanel mainPanel;

    public MainWindow() {
        super("Mystery-Box-Inator");

        try {
            setContentPane(mainPanel);
            var config = createConfig();
            var catalogue = new Catalogue(config); // for now, replace with loaded catalogue later.
            var dtm = configTable();
            setJMenuBar(new MenuBar(config, catalogue, dtm));
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1200, 800);
            setLocationRelativeTo(null);
            productTable.setModel(dtm);
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

    DefaultTableModel configTable() {
        return new DefaultTableModel(null, new Object[]{"Name", "Quantity", "Type", "Rarity", "Url", "Paste-Ups?", "Raw Cost", "Retail Price", "Profit"});
    }
}
