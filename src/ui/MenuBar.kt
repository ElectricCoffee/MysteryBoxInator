package ui

import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem

class MenuBar() : JMenuBar() {
    init {
        val fileMenu = JMenu("File")
        fileMenu.accessibleContext.accessibleDescription = "This menu handles file operations"
        this.add(fileMenu)

        val loadCatalogueCsvItem = JMenuItem("Load Catalogue CSV...")
        loadCatalogueCsvItem.accessibleContext.accessibleDescription = "Lets you load the CSV with all the games"
        fileMenu.add(loadCatalogueCsvItem)

        val editConfigItem = JMenuItem("Edit Config File...")
        editConfigItem.accessibleContext.accessibleDescription = "Opens the config file in your text editor of choice"
        fileMenu.add(editConfigItem)

        val openOutputFolderItem = JMenuItem("Open Output Folder...")
        openOutputFolderItem.accessibleContext.accessibleDescription = "Opens the output folder on your computer"
        fileMenu.add(openOutputFolderItem)
    }
}