package ui.util

import catalogue.Catalogue
import config.Config
import io.Filing
import javax.swing.table.DefaultTableModel

object TableUtils {
    @JvmStatic
    fun populateTable(config: Config, catalogue: Catalogue, dtm: DefaultTableModel) {
        dtm.dataVector.removeAllElements() // clear table before inserting

        for (e in catalogue.gamesList.values) {
            dtm.addRow(e.toTableArray())
        }

        // after the table populates, update the working copy
        Filing.writeWorkingCopy(config, catalogue)
    }
}