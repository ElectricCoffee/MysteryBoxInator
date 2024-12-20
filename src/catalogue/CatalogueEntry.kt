package catalogue

import game.Game
import common.GameCategory
import common.GameRarity
import common.HrBoolean
import common.HrPrice
import errors.CsvParsingException
import errors.UnknownPasteUpsException
import util.NumUtils
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URL
import java.util.Vector

data class CatalogueEntry(val game: Game, val quantity: Int = 1) {
    val title: String
        get() = game.title

    val retailValue: BigDecimal
        get() = game.retailValue

    val profit: BigDecimal
        get() = retailValue - game.importCost

    val totalValue: BigDecimal
        get() = retailValue * quantity.toBigDecimal()

    val totalProfit: BigDecimal
        get() = profit * quantity.toBigDecimal()

    fun addGame(extra: Int = 1) = CatalogueEntry(game, quantity + extra)

    fun updateQuantity(updater: (Int) -> Int): CatalogueEntry {
        return CatalogueEntry(game, updater(quantity))
    }

    /**
     * Creates the sort of array you want to see in the output csv
     */
    fun toStringArray(): Array<String> =
        arrayOf(
            game.title,
            quantity.toString(),
            game.gameCategory.toHumanReadable().toShortString(),
            game.rarity.value.toString(),
            game.bggURL?.toString() ?: "",
            if (game.requiresPasteUps) "TRUE" else "FALSE",
            game.importCost.setScale(2, RoundingMode.HALF_UP).toString(),
            game.retailValue.setScale(2, RoundingMode.HALF_UP).toString()
        )

    /**
     * Creates the sort of array you'd expect to be used in the table.
     */
    fun toTableArray(): Array<Any> {
        return arrayOf(
            game.title,
            Integer.valueOf(quantity),
            game.gameCategory.toHumanReadable(),
            game.rarity.toHumanReadable(),
            game.bggId ?: "N/A",
            HrBoolean(game.requiresPasteUps),
            HrPrice(game.importCost),
            HrPrice(game.retailValue),
            HrPrice(game.profit),
            HrPrice(totalProfit),
        )
    }

    fun toTableVector(): Vector<Any> {
        return Vector(toTableArray().toList())
    }

    companion object {
        @Throws(CsvParsingException::class)
        fun fromCsvLine(delimiter: String, line: String): CatalogueEntry {
            val chunks = line.split(delimiter).map { it.trim() }
            try {
                val name = chunks[0]
                val quantity = chunks[1].toInt()
                val type = GameCategory.fromString(chunks[2])
                val rarity = GameRarity.fromInt(chunks[3].toInt())
                val url = if (chunks[4].isNotEmpty()) URL(chunks[4]) else null
                val pasteUps = when (chunks[5].lowercase()) {
                    "yes", "true" -> true
                    "no", "false" -> false
                    else -> throw UnknownPasteUpsException(chunks[5])
                }
                val cost = BigDecimal(chunks[6])
                val price = BigDecimal(chunks[7])

                val game = Game(name, type, rarity, url, pasteUps, cost, price)
                return CatalogueEntry(game, quantity)
            } catch (nfe: NumberFormatException) {
                throw CsvParsingException(chunks)
            }
        }
    }
}