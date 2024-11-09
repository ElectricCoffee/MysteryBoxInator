package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainWindow extends JFrame {
    private JTabbedPane tabPane;
    private JTable productTable;
    private JPanel mainPanel;

    public MainWindow() {
        super("Mystery-Box-Inator");
        setContentPane(mainPanel);
        setJMenuBar(new MenuBar());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        DefaultTableModel dtm = new DefaultTableModel(null, new Object[]{ "Title", "Value £", "Category, URL"});
    }
}
