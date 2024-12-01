package ui

import catalogue.Catalogue
import catalogue.CatalogueEntry
import config.Config
import io.Filing.writeWorkingCopy
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.Vector
import javax.swing.JTable
import javax.swing.table.DefaultTableModel

class CatalogueEditButtonActionListener(private val config: Config, private val catalogue: Catalogue, private val productTable: JTable) : ActionListener {
    override fun actionPerformed(e: ActionEvent) {
        val i: Int = productTable.selectedRow
        val catalogueDtm = productTable.model as DefaultTableModel
        val title = catalogueDtm.getValueAt(i, 0) as String // the 0th column is the title
        val item: CatalogueEntry? = catalogue.getEntry(title)
        val newCatalogueItem = EditItemDialog.openDialog(item) ?: return

        checkNotNull(item)
        if (item.title == newCatalogueItem.title) {
            catalogue.setEntry(title, newCatalogueItem)
        } else {
            // if the title changed due to the update, remove the old entry from the list to avoid polluting the catalogue
            catalogue.setEntry(newCatalogueItem.title, newCatalogueItem)
            catalogue.deleteEntry(title)
        }

        catalogueDtm.dataVector[i] = Vector(newCatalogueItem.toTableArray().toList())

        writeWorkingCopy(config, catalogue)
        catalogueDtm.fireTableDataChanged()
    }
}