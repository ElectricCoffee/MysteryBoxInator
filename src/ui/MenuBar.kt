package ui

import catalogue.Catalogue
import catalogue.CsvLoadMode
import config.Config
import config.configFolderPath
import ui.util.CsvUtils
import java.awt.Desktop
import java.io.File
import java.math.RoundingMode
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.table.DefaultTableModel

class MenuBar(private val config: Config, private val catalogue: Catalogue, private val dtm: DefaultTableModel) : JMenuBar() {
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

    private fun helpMenu(): JMenu {
        val menu = JMenu("Help")
        menu.accessibleContext.accessibleDescription = "This menu showcases info for the user"

        val aboutItem = JMenuItem("About")
        aboutItem.accessibleContext.accessibleDescription = "Info about the software"
        aboutItem.addActionListener { onLoadAbout() }
        menu.add(aboutItem)

        return menu;
    }

    private fun onLoadAbout() {
        InfoDialog.openDialog();
    }

    init {
        this.add(fileMenu())
        this.add(helpMenu())
    }

    private fun onLoadCsv() {
        // should open file dialogue, where the user can then add their csv file of choice
        val dialog = CsvDialog()
        val result = dialog.openDialog()

        if (result == CsvActionSelected.CANCEL) {
            return
        }

        val file = dialog.selectedFile

        if (result == CsvActionSelected.APPEND) {
            catalogue.appendFromFile(file.toPath(), CsvLoadMode.APPEND, 1) // making this explicit here so it's clear it wasn't a mistake
        } else if (result == CsvActionSelected.OVERWRITE) {
            catalogue.appendFromFile(file.toPath(), CsvLoadMode.OVERWRITE, 1)
        }

        CsvUtils.populateTable(config, catalogue, dtm)
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