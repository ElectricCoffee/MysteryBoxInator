import common.Budget
import common.GameCategory
import common.GameRarity
import config.defaultConfig
import game.Game
import mysteryBox.MysteryBox
import mysteryBox.MysteryBoxList
import mysteryBox.assembler.TrickTakingBoxAssembler
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.UUID

class MysteryBoxTest {
    // todo: check various item picker logics
    @Test fun `can assemble a mystery box`() {
        val catalogue = ResourceHelper.getTestCatalogue()
        val assembler = TrickTakingBoxAssembler(defaultConfig, catalogue, BigDecimal("135.00"))
        val box = assembler.generateBox()

        Assertions.assertEquals(box.boxType, GameCategory.TRICK_TAKER, "The box type should be Trick Taker")
        Assertions.assertTrue(box.items.count { it.gameCategory == GameCategory.VARIETY } < 2, "There should be 0-1 variety games in this box")
        Assertions.assertTrue(box.items.count { it.rarity == GameRarity.MYTHIC } < 2, "There should be 0-1 mythic games in the box")
        Assertions.assertTrue(box.items.count() < 15, "There are 15 trick-takers below mythic rarity in the catalogue. at a value of 135 it should pick less than that")
    }
    
    @Test fun `can create a toml file of a mystery box`() {
        val games = listOf(
            Game("Niner", GameCategory.TRICK_TAKER, GameRarity.RARE, null, false, BigDecimal("12.99"), BigDecimal("24.99")),
            Game("Fishing", GameCategory.TRICK_TAKER, GameRarity.UNCOMMON, null, false, BigDecimal("12.99"), BigDecimal("24.99")),
            Game("Cho-Han Trick-Taking Game", GameCategory.TRICK_TAKER, GameRarity.COMMON, null, false, BigDecimal("13"), BigDecimal("25")),
            Game("MAD", GameCategory.TRICK_TAKER, GameRarity.RARE, null, false, BigDecimal("16"), BigDecimal("26")),
            Game("Prey", GameCategory.TRICK_TAKER, GameRarity.UNCOMMON, null, false, BigDecimal("16"), BigDecimal("26"))
        )

        val id = UUID.fromString("fba50200-33e5-44d9-8d87-f000a677bdd0")

        val mysteryBox = MysteryBox(id, games, BigDecimal("135.00"), GameCategory.TRICK_TAKER, Budget.UnderBudget(BigDecimal("-0.059")), false)

        val boxList = MysteryBoxList()
        boxList.addBox(mysteryBox)

        val actualString = boxList.toToml()

        val expectedString = """
            [mysteryBoxes.fba50200-33e5-44d9-8d87-f000a677bdd0]
            targetValue = 135.00
            boxType = "Trick-Taker"
            sold = false

            [mysteryBoxes.fba50200-33e5-44d9-8d87-f000a677bdd0.id]
            mostSigBits = -313842399138462503
            leastSigBits = -8248360306922242608

            [[mysteryBoxes.fba50200-33e5-44d9-8d87-f000a677bdd0.items]]
            title = "Niner"
            gameCategory = "Trick-Taker"
            rarity = "Rare (3)"
            requiresPasteUps = false
            importCost = 12.99
            retailValue = 24.99
            safeBggUrl = ""

            [[mysteryBoxes.fba50200-33e5-44d9-8d87-f000a677bdd0.items]]
            title = "Fishing"
            gameCategory = "Trick-Taker"
            rarity = "Uncommon (2)"
            requiresPasteUps = false
            importCost = 12.99
            retailValue = 24.99
            safeBggUrl = ""

            [[mysteryBoxes.fba50200-33e5-44d9-8d87-f000a677bdd0.items]]
            title = "Cho-Han Trick-Taking Game"
            gameCategory = "Trick-Taker"
            rarity = "Common (1)"
            requiresPasteUps = false
            importCost = 13
            retailValue = 25
            safeBggUrl = ""

            [[mysteryBoxes.fba50200-33e5-44d9-8d87-f000a677bdd0.items]]
            title = "MAD"
            gameCategory = "Trick-Taker"
            rarity = "Rare (3)"
            requiresPasteUps = false
            importCost = 16
            retailValue = 26
            safeBggUrl = ""

            [[mysteryBoxes.fba50200-33e5-44d9-8d87-f000a677bdd0.items]]
            title = "Prey"
            gameCategory = "Trick-Taker"
            rarity = "Uncommon (2)"
            requiresPasteUps = false
            importCost = 16
            retailValue = 26
            safeBggUrl = ""

            [mysteryBoxes.fba50200-33e5-44d9-8d87-f000a677bdd0.budgetStatus]
            amount = -0.059
            
            """.trimIndent()

        Assertions.assertEquals(expectedString, actualString, "Must match the generated toml data")
    }
}