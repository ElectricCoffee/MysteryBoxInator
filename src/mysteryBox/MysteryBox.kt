package mysteryBox

import Budget
import Game
import GameCategory
import util.NumUtils
import java.math.BigDecimal
import java.util.*

data class MysteryBox(val id: UUID, val items: List<Game>, val targetValue: BigDecimal, val boxType: GameCategory, val budgetStatus: Budget = Budget.OnBudget) {
    constructor(items: List<Game>, targetValue: BigDecimal, boxType: GameCategory, budgetStatus: Budget)
            : this(UUID.randomUUID(), items, targetValue, boxType, budgetStatus)

    val totalValue: BigDecimal
        get() = items.sumOf { it.retailValue }

    val totalProfit: BigDecimal
        get() = items.sumOf { it.profit }

    fun toTableArray(): Array<Any> {
//        arrayOf<Any>("Id", "Items", "Type", "Sell Price", "Budget", "Budget Status"));

        return arrayOf(
            id.toString(),
            items.joinToString(", ") { it.title },
            boxType.toString(),
            NumUtils.asPrice(items.sumOf { it.retailValue }),
            NumUtils.asPrice(targetValue),
            budgetStatus.toPercentString()
        )
    }
}