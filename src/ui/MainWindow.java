package ui;

import catalogue.Catalogue;
import config.Config;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.catalog.Catalog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static config.ConfigKt.*;

public class MainWindow extends JFrame {
    private JTabbedPane tabPane;
    private JTable productTable;
    private JPanel mainPanel;

    Config createConfig() throws IOException {
        String homeFolder = System.getProperty("user.home");

        Path configFolder = Paths.get(configFolderPath);
        Path configFile = Paths.get(configFilePath);

        if (!Files.exists(configFolder)) {
            Files.createDirectory(configFolder);
        }

        if (!Files.exists(configFile)) {
            Files.createFile(configFile);

            Files.write(configFile, defaultConfigString.getBytes());
        }

        return Config.Companion.fromFile(configFile);
    }

    public MainWindow() {
        super("Mystery-Box-Inator");

        try {
            setContentPane(mainPanel);
            Config config = createConfig();
            Catalogue catalogue = new Catalogue(config); // for now, replace with loaded catalogue later.
            setJMenuBar(new MenuBar(config, catalogue));
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1200, 800);
            setLocationRelativeTo(null);

            DefaultTableModel dtm = new DefaultTableModel(null, new Object[]{"Title", "Value £", "Category, URL"});
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this, ioe.getMessage(), "File Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
