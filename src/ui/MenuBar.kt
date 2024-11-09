package ui

import config.Config
import config.configFolderPath
import java.awt.Desktop
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import java.lang.IllegalArgumentException
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem

class MenuBar(private val config: Config) : JMenuBar() {
    private fun fileMenu(): JMenu {
        val menu = JMenu("File")
        menu.accessibleContext.accessibleDescription = "This menu handles file operations"

        val loadCatalogueCsvItem = JMenuItem("Load catalogue.Catalogue CSV...")
        loadCatalogueCsvItem.accessibleContext.accessibleDescription = "Lets you load the CSV with all the games"
        menu.add(loadCatalogueCsvItem)

        val editConfigItem = JMenuItem("Open Config Folder...")
        editConfigItem.accessibleContext.accessibleDescription =
            "Opens the config file in your text editor of choice"
        editConfigItem.addActionListener(ConfigFolderListener())
        menu.add(editConfigItem)

        val openOutputFolderItem = JMenuItem("Open Output Folder...")
        openOutputFolderItem.accessibleContext.accessibleDescription = "Opens the output folder on your computer"
        openOutputFolderItem.addActionListener(OutputFolderListener(config))
        menu.add(openOutputFolderItem)

        return menu;
    }
    init {
        this.add(fileMenu())
    }
}