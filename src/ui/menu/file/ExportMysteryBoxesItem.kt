package ui.menu.file

import config.Config
import io.Filing
import mysteryBox.MysteryBoxList
import java.io.IOException
import javax.swing.JMenuItem
import javax.swing.JOptionPane

class ExportMysteryBoxesItem(private val config: Config, private val mysteryBoxList: MysteryBoxList) : JMenuItem("Export Mystery Boxes...") {
    init {
        getAccessibleContext().accessibleDescription = "Exports the current mystery box list to the output folder"

        addActionListener { exportMysteryBoxes() }
    }

    private fun exportMysteryBoxes() {
        try {
            val path = Filing.writeMysteryBoxes(config, mysteryBoxList)
            JOptionPane.showMessageDialog(this, "Mystery boxes exported to $path.", "Success!", JOptionPane.INFORMATION_MESSAGE)
        } catch (ioe: IOException) {
            JOptionPane.showMessageDialog(this, ioe.message, "Error", JOptionPane.ERROR_MESSAGE)
        }
    }
}