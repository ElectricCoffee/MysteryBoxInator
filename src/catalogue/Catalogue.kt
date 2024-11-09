package catalogue

import Config
import Game
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path

class Catalogue {
    val gamesList = mutableMapOf<String, CatalogueEntry>();

    fun addGame(game: Game) {
        val title = game.title;
        gamesList[title] = gamesList[title]?.addGame() ?: CatalogueEntry(game)
    }

    fun getGame(title: String) = gamesList[title]?.game;
    fun getQuantity(title: String) = gamesList[title]?.quantity;
    fun getRetailValue(title: String) = gamesList[title]?.retailValue

    fun getTotalValue(title: String): BigDecimal {
        val entry = gamesList[title] ?: return BigDecimal.ZERO;

        return entry.totalValue
    }

    fun getCatalogueValue() = gamesList.values.sumOf { it.totalValue }

    val countGames: Int
        get() = gamesList.count()

    val countTotalInventory: Int
        get() = gamesList.values.sumOf { it.quantity }

    companion object {
        fun fromFile(config: Config, path: Path): Catalogue {
            val content = Files.readAllLines(path)
            return fromCsv(config, content)
        }

        fun fromCsv(config: Config, lines: List<String>): Catalogue {
            val entries = lines.map { CatalogueEntry.fromCsvLine(config.io.csvDelimiter, it) }
            val catalogue = Catalogue()

            entries.forEach {
                catalogue.gamesList[it.title] = it
            }

            return catalogue
        }
    }
}