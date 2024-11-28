package ui.menu.file

import config.configFolderPath
import ui.util.ErrorDialog
import java.awt.Desktop
import java.io.File
import javax.swing.JMenuItem

class EditConfigItem : JMenuItem("Open Config Folder...") {
    init {
        getAccessibleContext().accessibleDescription =
            "Opens the config file in your text editor of choice"
        addActionListener { onOpenConfigFolder() }
    }

    private fun onOpenConfigFolder() {
        try {
            val desktop = Desktop.getDesktop()
            desktop.open(File(configFolderPath))
        } catch (iae: IllegalArgumentException) {
            ErrorDialog(this).open(iae)
        }
    }
}