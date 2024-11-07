import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.math.BigDecimal
import java.net.URL

class CatalogueTest {
    @Test fun `Can add games to catalogue`() {
        val catalogue = Catalogue()

        catalogue.addGame(Game(
            "The Gang",
            GameCategory.VARIETY,
            GameRarity.COMMON,
            URL("https://boardgamegeek.com/boardgame/411567/the-gang"),
            false,
            BigDecimal(15),
            BigDecimal(20)
        ))

        // add a second copy of the gang
        catalogue.addGame(Game(
            "The Gang",
            GameCategory.VARIETY,
            GameRarity.COMMON,
            URL("https://boardgamegeek.com/boardgame/411567/the-gang"),
            false,
            BigDecimal(15),
            BigDecimal(20)
        ))

        catalogue.addGame(Game(
            "Spy Tricks",
            GameCategory.TRICK_TAKER,
            GameRarity.RARE,
            URL("https://boardgamegeek.com/boardgame/195163/spy-tricks"),
            false,
            BigDecimal(14),
            BigDecimal(18)
        ))

        assertEquals(2, catalogue.countGames,
            "Expected there to be two different games")
        assertEquals(3, catalogue.countTotalInventory,
            "Expect there to be three total games")
        assertEquals(BigDecimal(58), catalogue.getCatalogueValue(),
            "Expected the sum of all the games' value to be 58")
        assertEquals(2, catalogue.getQuantity("The Gang"),
            "Expected there to be two copies of 'The Gang'")
        assertEquals(BigDecimal(20), catalogue.getRetailValue("The Gang"),
            "Expected the retail value of 'The Gang' to be 20.00")
        assertEquals(BigDecimal(40), catalogue.getTotalValue("The Gang"),
            "Expected the total retail value of 'The Gang' to be 40.00")
    }

    @Test fun `Can parse CSV files`() {
        val csv = """
            The Gang, 3, Variety, 1, https://boardgamegeek.com/boardgame/411567/the-gang, no, 14.95, 19.95
            Spy Tricks, 2, Trick-Taker, 2, https://boardgamegeek.com/boardgame/195163/spy-tricks, no, 14.00, 18.00
        """.trimIndent()

        val catalogue = Catalogue.fromCsv(",", csv.split("\n"))

        assertEquals(2, catalogue.countGames,
            "Expected there to be two different games")
        assertEquals(5, catalogue.countTotalInventory,
            "Expect there to be five total games")
        assertEquals(BigDecimal("95.85"), catalogue.getCatalogueValue(),
            "Expected the sum of all the games' value to be 95.85")
        assertEquals(3, catalogue.getQuantity("The Gang"),
            "Expected there to be three copies of 'The Gang'")
        assertEquals(BigDecimal("19.95"), catalogue.getRetailValue("The Gang"),
            "Expected the retail value of 'The Gang' to be 20.00")
        assertEquals(BigDecimal("59.85"), catalogue.getTotalValue("The Gang"),
            "Expected the total retail value of 'The Gang' to be 59.85")
    }
}