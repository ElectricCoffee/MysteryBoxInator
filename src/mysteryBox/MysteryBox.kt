package mysteryBox

import Budget
import Game
import GameCategory
import java.math.BigDecimal

data class MysteryBox(val items: List<Game>, val targetValue: BigDecimal, val boxType: GameCategory, val budgetStatus: Budget = Budget.OnBudget) {
    val totalValue: BigDecimal
        get() = items.sumOf { it.retailValue }

    val totalProfit: BigDecimal
        get() = items.sumOf { it.profit }
}