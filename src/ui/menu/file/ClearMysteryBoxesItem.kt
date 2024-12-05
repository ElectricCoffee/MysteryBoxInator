package ui.menu.file

import config.Config
import io.Filing
import mysteryBox.MysteryBoxList
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.table.DefaultTableModel

class ClearMysteryBoxesItem(private val config: Config, private val mysteryBoxList: MysteryBoxList, private val dtm: DefaultTableModel) : JMenuItem("Clear Mystery Box List...") {
    init {
        getAccessibleContext().accessibleDescription = "Clears the Mystery Box List"
        addActionListener { onClearMysteryBoxes() }
    }

    private fun onClearMysteryBoxes() {
        val confirm1 = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to clear the list of mystery boxes?",
            "Warning, you're about to delete stuff!",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE)

        if (confirm1 != 0) {
            return
        }

        val confirm2 = JOptionPane.showConfirmDialog(
            this,
            "Are you sure that you're sure?",
            "No turning back now!",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE)

        if (confirm2 != 0) {
            return
        }

        Filing.backupMysteryBoxWorkingCopy(config)
        mysteryBoxList.mysteryBoxes.clear()
        dtm.dataVector.removeAllElements()
        dtm.fireTableDataChanged()
    }
}