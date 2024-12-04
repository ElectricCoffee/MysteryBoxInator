package mysteryBox

import common.*
import game.Game
import util.NumUtils
import java.math.BigDecimal
import java.util.*

data class MysteryBox(
    val id: String,
    val items: List<Game>,
    val targetValue: BigDecimal,
    val shortLabel: String,
    val boxType: GameCategory,
    val budgetStatus: Budget = Budget(),
    var packed: Boolean = false)
{
    constructor(items: List<Game>, targetValue: BigDecimal, shortLabel: String, boxType: GameCategory, budgetStatus: Budget, packed: Boolean)
            : this(UUID.randomUUID().toString(), items, targetValue, shortLabel, boxType, budgetStatus, packed)

    val count get() = items.count()

    val totalValue: BigDecimal
        get() = items.sumOf { it.retailValue }

    val totalProfit: BigDecimal
        get() = items.sumOf { it.profit }

    fun toTableArray(): Array<Any> {
//        arrayOf<Any>("Id", "Items", "Type", "Sell Price", "Budget", "Budget Status"));

        return arrayOf(
            id,
            Integer.valueOf(items.count()),
            items.joinToString(", ") { it.title },
            boxType.toHumanReadable(),
            HrPrice(totalValue),
            HrPrice(targetValue),
            "Off by ${NumUtils.asPrice((targetValue - totalValue).abs())} (${budgetStatus.toPercentString()})",
            HrBoolean(packed)
        )
    }

    /**
     * The box's prefix of the format SL-ST-uuid5, where
     * - SL is the short label defined in the config
     * - ST is the short version of the box type
     * - uuid5 is the first digits of the UUID.
     *
     * It is used in the output file format as well as the header of the viewer
     */
    fun getPrefix(): String {
        return "$shortLabel-${boxType.toHumanReadable().toShortString()}-${HrId(id)}"
    }
}