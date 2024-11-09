package ui;

import config.Config;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static config.ConfigKt.defaultConfig;
import static config.ConfigKt.defaultConfigString;

public class MainWindow extends JFrame {
    private JTabbedPane tabPane;
    private JTable productTable;
    private JPanel mainPanel;

    Config createConfig() throws IOException {
        String homeFolder = System.getProperty("user.home");
        String configFolderPath = homeFolder + File.separator + ".mystery-box-inator";
        String configFilePath = configFolderPath + File.separator + "config.toml";

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
            setJMenuBar(new MenuBar(config));
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1200, 800);
            setLocationRelativeTo(null);

            DefaultTableModel dtm = new DefaultTableModel(null, new Object[]{"Title", "Value £", "Category, URL"});
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this, ioe.getMessage(), "File Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
