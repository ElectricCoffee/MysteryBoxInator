import catalogue.Catalogue
import common.GameCategory
import common.GameRarity
import config.defaultConfig
import mysteryBox.MysteryBox
import mysteryBox.assembler.MysteryBoxAssemblerABC
import org.junit.jupiter.api.Test
import java.math.BigDecimal

// inheriting from the base class should let me get access to the protected members and test on them directly... in theory
class MysteryBoxAssemblerTestHelper(catalogue: Catalogue, budget: BigDecimal) : MysteryBoxAssemblerABC(defaultConfig, catalogue, budget) {
    // god, how do I even unit test something random?
    override fun generateBox(): MysteryBox {
        TODO("Not yet implemented")
    }

    fun PickTrickTaker(upperLimit: Boolean) = pickTrickTaker(upperLimit)
}

class MysteryBoxAssemblerTest {
    // todo: check various item picker logics
    @Test fun `can pick an item`() {
        val games = listOf(
            Game("foo", GameCategory.TRICK_TAKER, GameRarity.COMMON, null, true, BigDecimal(10), BigDecimal(15)),
            Game("bar", GameCategory.VARIETY, GameRarity.COMMON, null, true, BigDecimal(10), BigDecimal(15))
        )

        val catalogue = Catalogue(defaultConfig, games)

        val assembler = MysteryBoxAssemblerTestHelper(catalogue, BigDecimal(20))
    }
}