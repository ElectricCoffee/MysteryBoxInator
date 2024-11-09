package ui

import config.Config
import java.awt.Desktop
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import java.lang.IllegalArgumentException

class OutputFolderListener(private val config: Config) : ActionListener {
    override fun actionPerformed(e: ActionEvent?) {
        try {
            val desktop = Desktop.getDesktop()
            desktop.open(File(config.io.outputDirectory))
        } catch (iae: IllegalArgumentException) {
            TODO("Implement popup")
        }
    }
}