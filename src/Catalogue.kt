import java.math.BigDecimal

data class CatalogueEntry(val game: Game, val amount: BigDecimal = BigDecimal.ONE) {
    fun addGame(extra: BigDecimal = BigDecimal.ONE) = CatalogueEntry(game, amount + extra)
    fun itemValue() = game.retailValue;
    fun totalValue() = itemValue() * amount;
}

class Catalogue {
    val gamesList = mutableMapOf<String, CatalogueEntry>();

    fun addGame(game: Game) {
        val title = game.title;
        gamesList[title] = gamesList[title]?.addGame() ?: CatalogueEntry(game)
    }

    fun getGame(title: String) = gamesList[title]?.game;
    fun getAmount(title: String) = gamesList[title]?.amount;
    fun getIndividualValue(title: String) = gamesList[title]?.itemValue()

    fun getTotalValue(title: String): BigDecimal {
        val entry = gamesList[title] ?: return BigDecimal.ZERO;

        return entry.totalValue()
    }

    fun getCatalogueValue() = gamesList.values.sumOf { it.totalValue() }

    val countGames: Int
        get() = gamesList.count()

    val countTotalInventory: BigDecimal
        get() = gamesList.values.sumOf { it.amount }
}