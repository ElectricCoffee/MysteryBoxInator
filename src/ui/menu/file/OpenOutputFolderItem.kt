package ui.menu.file

import config.Config
import ui.util.ErrorDialog
import java.awt.Desktop
import java.io.File
import javax.swing.JMenuItem

class OpenOutputFolderItem(private val config: Config) : JMenuItem("Open Output Folder...") {
    init {
        getAccessibleContext().accessibleDescription = "Opens the output folder on your computer"
        addActionListener { onOpenOutputFolder(config) }
    }

    private fun onOpenOutputFolder(config: Config) {
        try {
            val desktop = Desktop.getDesktop()
            desktop.open(File(config.io.outputDirectory))
        } catch (iae: IllegalArgumentException) {
            ErrorDialog(this).open(iae)
        }
    }
}