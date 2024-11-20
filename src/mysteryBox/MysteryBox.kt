package mysteryBox

import Budget
import config.Config
import Game
import GameCategory
import java.math.BigDecimal

data class MysteryBox(val targetValue: BigDecimal, val boxType: GameCategory, val budgetStatus: Budget) {
    val items = listOf<Game>();

    val totalValue: BigDecimal
        get() = items.sumOf { it.retailValue }

    val totalProfit: BigDecimal
        get() = items.sumOf { it.profit }
}