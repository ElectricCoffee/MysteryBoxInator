package ui

import config.configFolderPath
import java.awt.Desktop
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import java.lang.IllegalArgumentException

class ConfigFolderListener() : ActionListener {
    override fun actionPerformed(e: ActionEvent?) {
        try {
            val desktop = Desktop.getDesktop()
            desktop.open(File(configFolderPath))
        } catch (iae: IllegalArgumentException) {
            TODO("Implement popup")
        }
    }
}