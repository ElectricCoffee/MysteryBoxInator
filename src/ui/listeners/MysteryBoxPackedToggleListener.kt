package ui.listeners

import config.Config
import io.Filing
import mysteryBox.MysteryBoxList
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JTable
import javax.swing.table.DefaultTableModel

class MysteryBoxPackedToggleListener(private val config: Config, private val mysteryBoxTable: JTable, private val mysteryBoxList: MysteryBoxList) : ActionListener {
    override fun actionPerformed(e: ActionEvent?) {
        val row = mysteryBoxTable.selectedRow
        val i = mysteryBoxTable.convertRowIndexToModel(row)
        val dtm = mysteryBoxTable.model as DefaultTableModel
        val id = dtm.getValueAt(i, 0) // 0 is the id column
        val mysteryBox = mysteryBoxList.mysteryBoxes[id] ?: return

        mysteryBox.packed = !mysteryBox.packed

        dtm.dataVector[i] = mysteryBox.toTableVector()
        Filing.writeWorkingCopy(config, mysteryBoxList)

        dtm.fireTableDataChanged()
    }
}