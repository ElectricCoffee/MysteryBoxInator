import java.net.MalformedURLException
import java.net.URL

/**
 * A Game object.
 * @param title The game's title. Its name.
 * @param gameCategory The game's category, either a Trick-Taker or a Variety game.
 * @param rarity How rare the game is.
 * @param bggURL The game's url on BoardGameGeek. Used to extract its game ID.
 * @param requiresPasteUps True if the game requires a paper manual or paste-ups.
 * @param importCost The raw price of the item.
 * @param retailValue The value of the game in pence.
 */
data class Game(
    val title: String,
    val gameCategory: GameCategory,
    val rarity: GameRarity,
    val bggURL: URL?,
    val requiresPasteUps: Boolean,
    val importCost: Int,
    val retailValue: Int,
) {

    /**
     * Gets the BGG game ID from the URL.
     * Every BGG Game's URL is structured as follows: https://boardgamegeek.com/boardgame/:id/:game-name
     * This function only cares about the :id part.
     *
     * @return Either a number (the game id) or null if that isn't found.
     */
    fun getBggId(): Int? {
        if (bggURL == null) {
            return null
        }


        val path = bggURL.path

        val re = """boardgame/(\d+)/""".toRegex()

        val result = re.find(path)
            ?: throw MalformedURLException("The URL provided was not a link to a BGG game. Make sure it is.")

        return result.groupValues[1].toInt()
    }

    val profit: Int
        get() = retailValue - importCost;
}