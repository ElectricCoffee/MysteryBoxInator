package ui.menu.file

import catalogue.Catalogue
import config.Config
import io.Filing
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.table.DefaultTableModel

class ClearCatalogueItem(private val config: Config, private val catalogue: Catalogue, private val dtm: DefaultTableModel) : JMenuItem("Clear Catalogue...") {
    init {
        getAccessibleContext().accessibleDescription = "Clears the catalogue"
        addActionListener { onClearCatalogue() }
    }

    private fun onClearCatalogue() {
        val dialogText = """
            <html>
                Are you sure you want to clear the catalogue?
                (A backup will be created)
            <html>
        """.trimIndent()
        val dialogResult = JOptionPane.showConfirmDialog(this,dialogText, "Warning!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)

        if (dialogResult != 0) {
            // zero is yes
            return
        }

        Filing.backupCatalogue(config, catalogue)
        Filing.deleteWorkingCopy(config)
        catalogue.gamesList.clear()
        dtm.dataVector.removeAllElements()
        dtm.fireTableDataChanged()
    }
}
