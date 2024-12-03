package ui.models

import common.HrBoolean
import common.HrGameCategory
import common.HrPrice
import javax.swing.table.DefaultTableModel

private val columns = arrayOf<Any>(
    "Id",               // 0
    "Units",            // 1
    "Items",            // 2
    "Type",             // 3
    "Total Item Value", // 4
    "Box Price",        // 5
    "Price Deviance",   // 6
    "Sold?"             // 7
)

class MysteryBoxTableModel : DefaultTableModel(null, columns) {
    override fun getColumnClass(columnIndex: Int): Class<*> {
        return when (columnIndex) {
            1 -> Integer::class.java
            3 -> HrGameCategory::class.java
            4, 5 -> HrPrice::class.java
            7 -> HrBoolean::class.java
            else -> super.getColumnClass(columnIndex)
        }
    }
}