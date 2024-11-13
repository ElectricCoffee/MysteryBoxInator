package ui

import config.Config
import config.configFolderPath
import java.awt.Desktop
import java.io.File
import java.lang.IllegalArgumentException
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JOptionPane

class MenuBar(private val config: Config) : JMenuBar() {
    private fun fileMenu(): JMenu {
        val menu = JMenu("File")
        menu.accessibleContext.accessibleDescription = "This menu handles file operations"

        val loadCatalogueCsvItem = JMenuItem("Load Catalogue CSV...")
        loadCatalogueCsvItem.accessibleContext.accessibleDescription = "Lets you load the CSV with all the games"
        loadCatalogueCsvItem.addActionListener { onLoadCsv() }
        menu.add(loadCatalogueCsvItem)

        val editConfigItem = JMenuItem("Open Config Folder...")
        editConfigItem.accessibleContext.accessibleDescription =
            "Opens the config file in your text editor of choice"
        editConfigItem.addActionListener { onOpenConfigFolder() }
        menu.add(editConfigItem)

        val openOutputFolderItem = JMenuItem("Open Output Folder...")
        openOutputFolderItem.accessibleContext.accessibleDescription = "Opens the output folder on your computer"
        openOutputFolderItem.addActionListener { onOpenOutputFolder(config) }
        menu.add(openOutputFolderItem)

        return menu;
    }
    init {
        this.add(fileMenu())
    }

    private fun onLoadCsv() {
        // should open file dialogue, where the user can then add their csv file of choice
        val dialog = CsvDialog()
        dialog.openDialog()
    }

    private fun onOpenOutputFolder(config: Config) {
        try {
            val desktop = Desktop.getDesktop()
            desktop.open(File(config.io.outputDirectory))
        } catch (iae: IllegalArgumentException) {
            openErrorDialog(iae)
        }
    }

    private fun onOpenConfigFolder() {
        try {
            val desktop = Desktop.getDesktop()
            desktop.open(File(configFolderPath))
        } catch (iae: IllegalArgumentException) {
            openErrorDialog(iae)
        }
    }

    private fun openErrorDialog(iae: IllegalArgumentException) {
        JOptionPane.showMessageDialog(this, iae.message, "Error", JOptionPane.ERROR_MESSAGE)
    }
}