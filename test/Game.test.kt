import common.GameCategory
import common.GameRarity
import game.Game
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.math.BigDecimal
import java.net.URL

class GameTest {
    @Test fun `test BGG ID function`() {
        val game = Game(
            "The Gang",
            GameCategory.VARIETY,
            GameRarity.COMMON,
            URL("https://boardgamegeek.com/boardgame/411567/the-gang"),
            false,
            BigDecimal(15),
            BigDecimal(20)
        )

        val expected = 411567
        val actual = game.bggId
        assertEquals(expected, actual, "Expected the bgg id to be 411567")
    }
}