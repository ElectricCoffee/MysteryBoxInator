package ui.util

import catalogue.Catalogue
import config.Config
import io.Filing
import mysteryBox.MysteryBox
import mysteryBox.MysteryBoxList
import javax.swing.table.DefaultTableModel

object TableUtils {
    @JvmStatic
    fun populateCatalogueTable(config: Config, catalogue: Catalogue, dtm: DefaultTableModel) {
        dtm.dataVector.removeAllElements() // clear table before inserting

        for (e in catalogue.gamesList.values) {
            dtm.addRow(e.toTableArray())
        }

        // after the table populates, update the working copy
        Filing.writeWorkingCopy(config, catalogue)
    }

    fun populateMysteryBoxTable(config: Config, mysteryBoxes: MysteryBoxList, dtm: DefaultTableModel) {
        dtm.dataVector.clear()
    }
}