import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainWindow extends JFrame {
    private JTabbedPane tabPane;
    private JTable productTable;
    private JPanel mainPanel;
    private JMenuBar menuBar;

    public MainWindow() {
        super("Mystery-Box-Inator");
        setContentPane(mainPanel);
        initMenuBar();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        DefaultTableModel dtm = new DefaultTableModel(null, new Object[]{ "Title", "Value £", "Category, URL"});
    }

    private void initMenuBar() {
        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.getAccessibleContext().setAccessibleDescription("This menu handles file operations");
        menuBar.add(fileMenu);

        JMenuItem loadCatalogueCsvItem = new JMenuItem("Load Catalogue CSV...");
        loadCatalogueCsvItem.getAccessibleContext().setAccessibleDescription("Lets you load the CSV with all the games");
        fileMenu.add(loadCatalogueCsvItem);

        JMenuItem editConfigItem = new JMenuItem("Edit Config File...");
        editConfigItem.getAccessibleContext().setAccessibleDescription("Opens the config file in your text editor of choice");
        fileMenu.add(editConfigItem);

        JMenuItem openOutputFolderItem = new JMenuItem("Open Output Folder...");
        openOutputFolderItem.getAccessibleContext().setAccessibleDescription("Opens the output folder on your computer");
        fileMenu.add(openOutputFolderItem);

        setJMenuBar(menuBar);
    }
}
