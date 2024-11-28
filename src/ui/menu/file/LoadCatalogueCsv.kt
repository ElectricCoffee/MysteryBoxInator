package ui.menu.file

import catalogue.Catalogue
import catalogue.CsvLoadMode
import config.Config
import ui.CsvActionSelected
import ui.CsvDialog
import ui.util.CsvUtils
import javax.swing.JMenuItem
import javax.swing.table.DefaultTableModel

class LoadCatalogueCsv(private val config: Config, private val catalogue: Catalogue, private val dtm: DefaultTableModel)
    : JMenuItem("Load Catalogue CSV...") {
    init {
        getAccessibleContext().accessibleDescription = "Lets you load the CSV with all the games"
        addActionListener { onLoadCsv() }
    }

    private fun onLoadCsv() {
        // should open file dialogue, where the user can then add their csv file of choice
        val dialog = CsvDialog()
        val result = dialog.openDialog()

        if (result == CsvActionSelected.CANCEL) {
            return
        }

        val file = dialog.selectedFile

        if (result == CsvActionSelected.APPEND) {
            catalogue.appendFromFile(file.toPath(), CsvLoadMode.APPEND, 1) // making this explicit here so it's clear it wasn't a mistake
        } else if (result == CsvActionSelected.OVERWRITE) {
            catalogue.appendFromFile(file.toPath(), CsvLoadMode.OVERWRITE, 1)
        }

        CsvUtils.populateTable(config, catalogue, dtm)
    }
}