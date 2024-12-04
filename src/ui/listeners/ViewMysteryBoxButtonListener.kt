package ui.listeners

import mysteryBox.MysteryBoxList
import ui.ViewMysteryBoxDialog
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JTable
import javax.swing.table.DefaultTableModel

class ViewMysteryBoxButtonListener(private val mysteryBoxList: MysteryBoxList, private val mysteryBoxTable: JTable) : ActionListener {
    override fun actionPerformed(e: ActionEvent?) {
        val row = mysteryBoxTable.selectedRow
        val i = mysteryBoxTable.convertRowIndexToModel(row)
        val dtm = mysteryBoxTable.model as DefaultTableModel
        val id = dtm.getValueAt(i, 0) // 0 is the id column
        val mysteryBox = mysteryBoxList.mysteryBoxes[id]

        ViewMysteryBoxDialog.openDialog(mysteryBox)
    }
}