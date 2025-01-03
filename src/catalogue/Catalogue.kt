package catalogue

import common.FileLoadMode
import config.Config
import game.Game
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path

class Catalogue(private val config: Config) {
    val gamesList = mutableMapOf<String, CatalogueEntry>();

    constructor(config: Config, games: List<Game>) : this(config) {
        addGames(games)
    }

    fun addGames(games: List<Game>) {
        for (game in games) {
            this.addGame(game)
        }
    }

    fun addGame(game: Game) {
        val title = game.title;
        gamesList[title] = gamesList[title]?.addGame() ?: CatalogueEntry(game)
    }

    fun getEntry(title: String) = gamesList[title]
    fun getGame(title: String) = gamesList[title]?.game
    fun getQuantity(title: String) = gamesList[title]?.quantity
    fun getRetailValue(title: String) = gamesList[title]?.retailValue

    fun getProfit(title: String): BigDecimal {
        val entry = gamesList[title] ?: return BigDecimal.ZERO

        return entry.profit
    }

    fun getTotalValue(title: String): BigDecimal {
        val entry = gamesList[title] ?: return BigDecimal.ZERO;

        return entry.totalValue
    }

    fun getTotalProfit(title: String): BigDecimal {
        val entry = gamesList[title] ?: return BigDecimal.ZERO;

        return entry.totalProfit
    }

    fun getCatalogueProfit() = gamesList.values.sumOf { it.totalProfit }

    fun getCatalogueValue() = gamesList.values.sumOf { it.totalValue }

    fun setEntry(title: String, catalogueEntry: CatalogueEntry) {
        gamesList[title] = catalogueEntry
    }

    fun updateQuantity(title: String, updater: (Int) -> Int): CatalogueEntry? {
        val entry = getEntry(title)?.updateQuantity(updater)

        if (entry != null) {
            gamesList[title] = entry
        }

        return entry
    }

    fun deleteEntry(title: String) {
        gamesList.remove(title)
    }

    val countGames: Int
        get() = gamesList.count()

    val countTotalInventory: Int
        get() = gamesList.values.sumOf { it.quantity }

    fun appendFromFile(path: Path, loadMode: FileLoadMode = FileLoadMode.APPEND, startIndex: Int = 0) {
        val content = Files.readAllLines(path)
        appendCsv(content, loadMode, startIndex)
    }

    fun appendCsv(lines: List<String>, loadMode: FileLoadMode = FileLoadMode.APPEND, startIndex: Int = 0) {
        val entries = lines
            .subList(startIndex, lines.size)
            .map { CatalogueEntry.fromCsvLine(config.io.csvDelimiter, it) }

        if (loadMode == FileLoadMode.OVERWRITE) {
            gamesList.clear()
        }

        entries.forEach {
            if (gamesList.containsKey(it.title)) {
                val old = gamesList[it.title]
                gamesList[it.title] = CatalogueEntry(it.game, old!!.quantity + it.quantity)
            } else {
                gamesList[it.title] = it
            }
        }
    }

    // note to self: we're exporting a list here because I don't want to deal with system dependent line endings.
    fun toCsv(includeHeader: Boolean): List<String> {
        val data = gamesList.values.map {
            it.toStringArray().joinToString(config.io.csvDelimiter)
        }

        return if(includeHeader) {
            listOf("game name,quantity,type,rarity,url,pasteups and paper rules,raw cost,retail price") + data
        } else {
            data
        }
    }

    companion object {
        fun fromFile(config: Config, path: Path, startIndex: Int = 0): Catalogue {
            val catalogue = Catalogue(config)

            catalogue.appendFromFile(path, startIndex = startIndex)

            return catalogue
        }

        fun fromCsv(config: Config, lines: List<String>, startIndex: Int = 0): Catalogue {
            val catalogue = Catalogue(config)

            catalogue.appendCsv(lines, startIndex = startIndex)

            return catalogue
        }
    }
}