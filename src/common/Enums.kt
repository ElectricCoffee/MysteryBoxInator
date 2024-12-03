package common

import errors.UnknownCategoryException
import mysteryBox.MysteryBox
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

enum class FileLoadMode {
    OVERWRITE, APPEND
}

/**
 * The type of game to be added to the mystery box
 */
enum class GameCategory {
    ACCESSORY,
    /**
     * The game's a trick-taker.
     */
    TRICK_TAKER,

    /**
     * The game's a variety game (not a trick-taker).
     */
    VARIETY;

    fun toHumanReadable(): HrGameCategory = HrGameCategory(this)

    companion object {
        fun fromString(string: String): GameCategory {
            return when (string.lowercase()[0]) {
                'a' -> ACCESSORY
                't' -> TRICK_TAKER
                'v' -> VARIETY
                else -> throw UnknownCategoryException(string)
            }
        }
    }
}

enum class ItemPickStatus {
    SUCCESS,
    FAILURE_NO_ITEMS,
    FAILURE_NOTHING_AFFORDABLE_AT_NORMAL_BUDGET,
    FAILURE_NOTHING_AVAILABLE_AT_RAISED_BUDGET
}

// used to be an enum-like data-structure, but isn't anymore.
// can't be arsed to move it.
data class Budget(val amount: BigDecimal) {
    constructor() : this(BigDecimal.ZERO)

    fun amountAsPercentage(): BigDecimal {
        return amount * (100).toBigDecimal()
    }

    fun toPercentString(): String {
        val format = DecimalFormat("+#,##0.00;-#,##0.00")
        return format.format(amountAsPercentage().setScale(2, RoundingMode.HALF_UP))
    }

    companion object {
        fun fromMoneySpent(moneySpent: BigDecimal, budget: BigDecimal) = Budget((moneySpent / budget) - BigDecimal(1))
    }
}

enum class GameRarity(val value: Int) {
    /**
     * Ok Game (e.g. cat poker)
     */
    COMMON(1),

    /**
     * Good Game/Excellent Easy to get game (e.g. Gummi Trick)
     */
    UNCOMMON(2),

    /**
     * Hard to Get/Well liked Designer/Limited Print Run/Availability (e.g. Eleven)
     */
    RARE(3),

    /**
     * The games people try (and fail to) bribe me over,
     * request incessantly for in their mystery box,
     * and go generally bananas for and I probably have 2-3 copies
     * (e.g. Of what's left, Kbernsitch), or a signed copy
     */
    MYTHIC(4);

    fun toHumanReadable() = HrGameRarity(this)

    companion object {
        fun fromInt(int: Int): GameRarity {
            return entries.first { it.value == int }
        }
    }
}

sealed class GenerateFailedDialogResult() {
    class Yes(val mb: MysteryBox) : GenerateFailedDialogResult()
    data object No : GenerateFailedDialogResult()
    data object TryAgain : GenerateFailedDialogResult()
}