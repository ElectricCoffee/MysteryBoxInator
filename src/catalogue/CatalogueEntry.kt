package catalogue

import Game
import GameCategory
import GameRarity
import errors.UnknownPasteUpsException
import java.math.BigDecimal
import java.net.URL

data class CatalogueEntry(val game: Game, val quantity: Int = 1) {
    val title: String
        get() = game.title

    val retailValue: BigDecimal
        get() = game.retailValue

    val totalValue: BigDecimal
        get() = retailValue * quantity.toBigDecimal()

    fun addGame(extra: Int = 1) = CatalogueEntry(game, quantity + extra)

    companion object {
        fun fromCsvLine(delimiter: String, line: String): CatalogueEntry {
            val chunks = line.split(delimiter, limit = 8).map { it.trim() }
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
        }
    }
}