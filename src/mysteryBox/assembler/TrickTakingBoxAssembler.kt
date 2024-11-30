package mysteryBox.assembler

import catalogue.Catalogue
import common.GameCategory
import common.GameRarity
import common.ItemPickStatus
import config.Config
import mysteryBox.MysteryBox
import java.math.BigDecimal

class TrickTakingBoxAssembler(config: Config, catalogue: Catalogue, value: BigDecimal, excludeVariety: Boolean = false)
    : MysteryBoxAssemblerABC(config, catalogue, value) {

    init {
        hasPickedVariety = excludeVariety
    }

    private fun pickSpecials() {
        pickSpecialMythic(trickTakers)
        pickSpecialVariety()
        pickSpecialAccessory()
    }

    override fun generateBox(): MysteryBox {
        pickSpecials()

        var tryAgainOverBudget = false

        while(true) {
            val result = when (pickNext(trickTakers)) {
                GameRarity.COMMON -> pickCommon(trickTakers, tryAgainOverBudget)
                GameRarity.UNCOMMON -> pickUncommon(trickTakers, tryAgainOverBudget)
                GameRarity.RARE, GameRarity.MYTHIC -> pickRare(trickTakers, tryAgainOverBudget)
            }

            when (result) {
                ItemPickStatus.SUCCESS -> continue
                ItemPickStatus.FAILURE_NO_ITEMS -> break
                ItemPickStatus.FAILURE_NOTHING_AFFORDABLE_AT_NORMAL_BUDGET -> {
                    tryAgainOverBudget = true
                    continue
                }
                ItemPickStatus.FAILURE_NOTHING_AVAILABLE_AT_RAISED_BUDGET -> {
                    break
                }
            }
        }

        return MysteryBox(pickedItems, budget, GameCategory.TRICK_TAKER, budgetStatus(), false)
    }
}