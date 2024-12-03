package ui.models

import common.HrGameCategory
import common.HrGameRarity
import javax.swing.table.DefaultTableModel

val columns = arrayOf<Any>(
    "Name",
    "Quantity",
    "Type",
    "Rarity",
    "BGG ID",
    "Paste-Ups?",
    "Raw Cost",
    "Retail Price",
    "Item Profit",
    "Total Profit"
)

class CatalogueTableModel : DefaultTableModel(null, columns) {
    override fun getColumnClass(columnIndex: Int): Class<*> {
        return when (columnIndex) {
            1 -> Integer::class.java
            2 -> HrGameCategory::class.java
            3 -> HrGameRarity::class.java
            else -> super.getColumnClass(columnIndex)
        }
    }
}