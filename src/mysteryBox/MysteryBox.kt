package mysteryBox

import common.Budget
import game.Game
import common.GameCategory
import util.NumUtils
import java.math.BigDecimal
import java.util.*

data class MysteryBox(val id: String, val items: List<Game>, val targetValue: BigDecimal, val boxType: GameCategory, val budgetStatus: Budget = Budget(), val sold: Boolean = false) {
    constructor(items: List<Game>, targetValue: BigDecimal, boxType: GameCategory, budgetStatus: Budget, sold: Boolean)
            : this(UUID.randomUUID().toString(), items, targetValue, boxType, budgetStatus, sold)

    val totalValue: BigDecimal
        get() = items.sumOf { it.retailValue }

    val totalProfit: BigDecimal
        get() = items.sumOf { it.profit }

    fun toTableArray(): Array<Any> {
//        arrayOf<Any>("Id", "Items", "Type", "Sell Price", "Budget", "Budget Status"));

        val totalRetailValue = items.sumOf { it.retailValue }

        return arrayOf(
            id.toString(),
            items.count(),
            items.joinToString(", ") { it.title },
            boxType.toString(),
            NumUtils.asPrice(totalRetailValue),
            NumUtils.asPrice(targetValue),
            "Off by ${NumUtils.asPrice(targetValue - totalRetailValue)} (${budgetStatus.toPercentString()})",
            if (sold) "Yes" else "No"
        )
    }
}