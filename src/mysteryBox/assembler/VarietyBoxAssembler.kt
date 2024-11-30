package mysteryBox.assembler

import catalogue.Catalogue
import common.GameCategory
import common.GameRarity
import common.ItemPickStatus
import config.Config
import mysteryBox.MysteryBox
import java.math.BigDecimal

class VarietyBoxAssembler(config: Config, catalogue: Catalogue, value: BigDecimal, excludeTrickTaker: Boolean = false)
    : MysteryBoxAssemblerABC(config, catalogue, value) {

    init {
        hasPickedTrickTaker = excludeTrickTaker
    }

    private fun pickSpecials() {
        pickSpecialMythic(varieties)
        pickSpecialTrickTaker()
        pickSpecialAccessory()
    }

    override fun generateBox(): MysteryBox {
        pickSpecials()

        var tryAgainOverBudget = false

        while(true) {
            val result = when (pickNext()) {
                GameRarity.COMMON -> pickCommon(varieties, tryAgainOverBudget)
                GameRarity.UNCOMMON -> pickUncommon(varieties, tryAgainOverBudget)
                GameRarity.RARE, GameRarity.MYTHIC -> pickRare(varieties, tryAgainOverBudget)
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

        return MysteryBox(pickedItems, budget, GameCategory.VARIETY, budgetStatus())
    }
}