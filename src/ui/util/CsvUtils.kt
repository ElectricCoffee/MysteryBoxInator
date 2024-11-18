package ui.util

import catalogue.Catalogue
import config.Config
import io.Filing
import java.math.BigDecimal
import java.math.RoundingMode
import javax.swing.table.DefaultTableModel

object CsvUtils {
    fun populateTable(config: Config, catalogue: Catalogue, dtm: DefaultTableModel) {
        dtm.dataVector.removeAllElements() // clear table before inserting

        for ((game, quan) in catalogue.gamesList.values) {
            val totalProfit = game.profit * BigDecimal(quan)
            dtm.addRow(
                arrayOf<Any>(
                    game.title,
                    quan.toString(),
                    game.gameCategory.toString(),
                    game.rarity.toString(),
                    game.bggURL?.toString() ?: "N/A",
                    if (game.requiresPasteUps) "Yes" else "No",
                    "£" + game.importCost.setScale(2, RoundingMode.HALF_UP).toString(),
                    "£" + game.retailValue.setScale(2, RoundingMode.HALF_UP).toString(),
                    "£" + game.profit.setScale(2, RoundingMode.HALF_UP).toString(),
                    "£" + totalProfit.setScale(2, RoundingMode.HALF_UP).toString()
                )
            )
        }

        // after the table populates, update the working copy
        Filing.writeWorkingCopy(config, catalogue)
    }
}