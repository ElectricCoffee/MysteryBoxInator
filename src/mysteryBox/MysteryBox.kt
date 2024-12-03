package mysteryBox

import common.Budget
import game.Game
import common.GameCategory
import common.HrBoolean
import common.HrPrice
import util.NumUtils
import java.math.BigDecimal
import java.util.*

// TODO: add short label to mystery box
data class MysteryBox(val id: String, val items: List<Game>, val targetValue: BigDecimal, val boxType: GameCategory, val budgetStatus: Budget = Budget(), val packed: Boolean = false) {
    constructor(items: List<Game>, targetValue: BigDecimal, boxType: GameCategory, budgetStatus: Budget, packed: Boolean)
            : this(UUID.randomUUID().toString(), items, targetValue, boxType, budgetStatus, packed)

    val totalValue: BigDecimal
        get() = items.sumOf { it.retailValue }

    val totalProfit: BigDecimal
        get() = items.sumOf { it.profit }

    fun toTableArray(): Array<Any> {
//        arrayOf<Any>("Id", "Items", "Type", "Sell Price", "Budget", "Budget Status"));

        val totalRetailValue = items.sumOf { it.retailValue }

        return arrayOf(
            id,
            Integer.valueOf(items.count()),
            items.joinToString(", ") { it.title },
            boxType.toHumanReadable(),
            HrPrice(totalRetailValue),
            HrPrice(targetValue),
            "Off by ${NumUtils.asPrice((targetValue - totalRetailValue).abs())} (${budgetStatus.toPercentString()})",
            HrBoolean(packed)
        )
    }
}