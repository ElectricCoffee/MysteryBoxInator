class CatalogueEntry(val game: Game, val amount: Int = 1) {
    fun addGame(extra: Int = 1) = CatalogueEntry(game, amount + extra)
    fun itemValue() = game.value;
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

    fun getTotalValue(title: String): Int {
        val entry = gamesList[title] ?: return 0;

        return entry.totalValue()
    }

    fun getCatalogueValue() = gamesList.values.sumOf { it.totalValue() }

    val countGames: Int
        get() = gamesList.count()

    val countTotalInventory: Int
        get() = gamesList.values.sumOf { it.amount }
}