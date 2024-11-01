import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.net.URL

class MysteryBoxTest {
    @Test fun `it successfully initialises a mystery box`() {
        val box = SmallMysteryBox(GameCategory.VARIETY)

        assertEquals(box.boxType, GameCategory.VARIETY, "Expected small variety box to have game category 'Variety'")
        assertEquals(box.targetValue, 4500, "Expected small variety box to have target value of £45.00")
        assertEquals(box.currentValue, 0, "Expected newly initialised variety box to have a total value of £0")
    }

    @Test fun `it successfully fails at adding too many games`() {
        val box = SmallMysteryBox(GameCategory.VARIETY)

        val `result 1` = box.addItem(
            Game(
                "The Gang",
                GameCategory.VARIETY,
                GameRarity.COMMON,
                URL("https://boardgamegeek.com/boardgame/411567/the-gang"),
                false,
                1500,
                2000
            )
        )

        assertEquals(box.currentValue, 2000, "Expect value to be £20 after adding one £20 game")

        assertEquals(`result 1`, ItemAddStatus.SUCCESS, "Expected value to be successfully added to start")

        val `result 2` = box.addItem(
            Game(
                "Shikoku 1889",
                GameCategory.VARIETY,
                GameRarity.RARE,
                URL("https://boardgamegeek.com/boardgame/23540/shikoku-1889"),
                false,
                6000,
                7900
            )
        )

        assertEquals(`result 2`, ItemAddStatus.EXCEEDS_TARGET_VALUE, "Expected the £79 game to exceed the £45 threshold")
    }
}