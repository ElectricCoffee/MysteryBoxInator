import config.defaultConfig
import mysteryBox.MysteryBox
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.math.BigDecimal
import java.net.URL

class MysteryBoxTest {
    @Test fun `it successfully initialises a mystery box`() {
        val box = MysteryBox(defaultConfig, BigDecimal(45), GameCategory.VARIETY)

        assertEquals(box.boxType, GameCategory.VARIETY, "Expected small variety box to have game category 'Variety'")
        assertEquals(box.targetValue, BigDecimal(45), "Expected small variety box to have target value of £45.00")
        assertEquals(box.currentValue, BigDecimal(0), "Expected newly initialised variety box to have a total value of £0")
    }

    @Test fun `it successfully fails at adding too many games`() {
        val box = MysteryBox(defaultConfig, BigDecimal(45), GameCategory.VARIETY)

        val `result 1` = box.addItem(
            Game(
                "The Gang",
                GameCategory.VARIETY,
                GameRarity.COMMON,
                URL("https://boardgamegeek.com/boardgame/411567/the-gang"),
                false,
                BigDecimal(15),
                BigDecimal("19.95")
            )
        )

        assertEquals(box.currentValue, BigDecimal("19.95"), "Expect value to be £19.95 after adding one £19.95 game")

        assertEquals(`result 1`, ItemAddStatus.SUCCESS, "Expected value to be successfully added to start")

        val `result 2` = box.addItem(
            Game(
                "Shikoku 1889",
                GameCategory.VARIETY,
                GameRarity.RARE,
                URL("https://boardgamegeek.com/boardgame/23540/shikoku-1889"),
                false,
                BigDecimal(60),
                BigDecimal("78.99")
            )
        )

        assertEquals(`result 2`, ItemAddStatus.EXCEEDS_TARGET_VALUE, "Expected the £79 game to exceed the £45 threshold")
    }
}