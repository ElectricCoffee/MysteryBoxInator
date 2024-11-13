package catalogue

import config.Config
import Game
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path

enum class CsvLoadMode {
    OVERWERITE, APPEND
}

class Catalogue(private val config: Config) {
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

    fun appendFromFile(path: Path, loadMode: CsvLoadMode = CsvLoadMode.APPEND) {
        val content = Files.readAllLines(path)
        appendCsv(content, loadMode)
    }

    fun appendCsv(lines: List<String>, loadMode: CsvLoadMode = CsvLoadMode.APPEND) {
        val entries = lines.map { CatalogueEntry.fromCsvLine(config.io.csvDelimiter, it) }

        if (loadMode == CsvLoadMode.OVERWERITE) {
            gamesList.clear()
        }

        entries.forEach {
            gamesList[it.title] = it
        }
    }

    companion object {
        fun fromFile(config: Config, path: Path): Catalogue {
            val catalogue = Catalogue(config)

            catalogue.appendFromFile(path)

            return catalogue
        }

        fun fromCsv(config: Config, lines: List<String>): Catalogue {
            val catalogue = Catalogue(config)

            catalogue.appendCsv(lines)

            return catalogue
        }
    }
}