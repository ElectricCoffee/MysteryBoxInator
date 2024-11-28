package ui.menu

import catalogue.Catalogue
import config.Config
import ui.InfoDialog
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.table.DefaultTableModel

class HelpMenu(private val config: Config, private val catalogue: Catalogue, private val dtm: DefaultTableModel) : JMenu("Help") {
    init {
        getAccessibleContext().accessibleDescription = "This menu showcases info for the user"

        val aboutItem = JMenuItem("About")
        aboutItem.getAccessibleContext().accessibleDescription = "Info about the software"
        aboutItem.addActionListener { onLoadAbout() }
        add(aboutItem)
    }

    private fun onLoadAbout() {
        InfoDialog.openDialog();
    }
}