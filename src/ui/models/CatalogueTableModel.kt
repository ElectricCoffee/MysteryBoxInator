package ui.models

import common.HrBoolean
import common.HrGameCategory
import common.HrGameRarity
import common.HrPrice
import javax.swing.table.DefaultTableModel

private val columns = arrayOf<Any>(
    "Name",         // 0
    "Quantity",     // 1
    "Type",         // 2
    "Rarity",       // 3
    "BGG ID",       // 4
    "Paste-Ups?",   // 5
    "Raw Cost",     // 6
    "Retail Price", // 7
    "Item Profit",  // 8
    "Total Profit"  // 9
)

class CatalogueTableModel : DefaultTableModel(null, columns) {
    override fun getColumnClass(columnIndex: Int): Class<*> {
        return when (columnIndex) {
            1 -> Integer::class.java
            2 -> HrGameCategory::class.java
            3 -> HrGameRarity::class.java
            5 -> HrBoolean::class.java
            6,7,8,9 -> HrPrice::class.java
            else -> super.getColumnClass(columnIndex)
        }
    }

    override fun isCellEditable(row: Int, column: Int): Boolean {
        return false
    }
}