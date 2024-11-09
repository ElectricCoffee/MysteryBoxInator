package mysteryBox

import config.Config
import Game
import GameCategory
import ItemAddStatus
import java.math.BigDecimal

// NB: targetValue is in pennies.
open class MysteryBox(config: Config, val targetValue: BigDecimal, val boxType: GameCategory) {
    var currentValue = BigDecimal.ZERO
        private set;

    val items = mutableListOf<Game>();

    val totalProfit: BigDecimal
        get() = items.sumOf { it.profit }

    private val targetValueUpperLimit = (targetValue + (targetValue * config.thresholds.upperAsFraction))
    private val targetValueLowerLimit = (targetValue - (targetValue * config.thresholds.lowerAsFraction)) // find use for this.

    // TODO: add box type to the inclusion process.... somehow
    fun addItem(item: Game): ItemAddStatus {
        val newCurrent = currentValue + item.retailValue;

        if (newCurrent > targetValueUpperLimit) {
            return ItemAddStatus.EXCEEDS_TARGET_VALUE;
        }

        items.add(item);
        currentValue = newCurrent
        return ItemAddStatus.SUCCESS;
    }
}