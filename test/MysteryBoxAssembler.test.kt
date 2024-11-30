import common.GameCategory
import common.GameRarity
import config.defaultConfig
import mysteryBox.assembler.TrickTakingBoxAssembler
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class MysteryBoxAssemblerTest {
    // todo: check various item picker logics
    @Test fun `can create a mystery box`() {
        val catalogue = ResourceHelper.getTestCatalogue()
        val assembler = TrickTakingBoxAssembler(defaultConfig, catalogue, BigDecimal("45.00"))
        val box = assembler.generateBox()

        Assertions.assertEquals(box.boxType, GameCategory.TRICK_TAKER, "The box type should be Trick Taker")
        Assertions.assertTrue(box.items.count { it.gameCategory == GameCategory.VARIETY } < 2, "There should be 0-1 variety games in this box")
        Assertions.assertTrue(box.items.count { it.rarity == GameRarity.MYTHIC } < 2, "There should be 0-1 mythic games in the box")
    }
}