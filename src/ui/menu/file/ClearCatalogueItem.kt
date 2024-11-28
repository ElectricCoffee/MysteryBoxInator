package ui.menu.file

import javax.swing.JMenuItem

class ClearCatalogueItem() : JMenuItem("Clear Catalogue...") {
    init {
        getAccessibleContext().accessibleDescription = "Clears the catalogue"
        addActionListener { onClearCatalogue() }
    }

    private fun onClearCatalogue() {
//        JOptionPane.showMessageDialog(parentComponent,"This is a test")
        println("This is a test")
    }
}
