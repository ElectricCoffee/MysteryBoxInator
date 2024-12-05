package ui.listeners

import config.Config
import io.Filing
import mysteryBox.MysteryBoxList
import ui.EditShippingInfoDialog
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JTable
import javax.swing.table.DefaultTableModel

class EditShippingInfoListener(private val config: Config, private val mysteryBoxList: MysteryBoxList, private val mysteryBoxTable: JTable) : ActionListener {
    override fun actionPerformed(e: ActionEvent?) {
        val row = mysteryBoxTable.selectedRow
        val i = mysteryBoxTable.convertRowIndexToModel(row)
        val dtm = mysteryBoxTable.model as DefaultTableModel
        val id = dtm.getValueAt(i, 0) as String // 0 is the id column
        val mysteryBox = mysteryBoxList.mysteryBoxes[id]

        val newBox = EditShippingInfoDialog.openDialog(mysteryBox)
        mysteryBoxList.mysteryBoxes[id] = newBox
        dtm.dataVector[i] = newBox.toTableVector()
        dtm.fireTableDataChanged()
        Filing.writeWorkingCopy(config, mysteryBoxList)
    }
}