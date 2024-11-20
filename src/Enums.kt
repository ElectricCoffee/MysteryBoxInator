import errors.UnknownCategoryException
import java.math.BigDecimal

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

    override fun toString(): String {
        return when (this.name) {
            "ACCESSORY" -> "Accessory"
            "TRICK_TAKER" -> "Trick-Taker"
            "VARIETY" -> "Variety"
            else -> this.name // default case just returns the raw name un-prettified.
        }
    }

    fun toShortString(): String {
        return when (this.name) {
            "ACCESSORY" -> "A"
            "TRICK_TAKER" -> "TT"
            "VARIETY" -> "V"
            else -> this.name // default case just returns the raw name un-prettified.
        }
    }

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

sealed class Budget(val amount: BigDecimal) {
    class OverBudget(amount: BigDecimal) : Budget(amount)
    class UnderBudget(amount: BigDecimal) : Budget(amount)
    data object OnBudget : Budget(BigDecimal.ZERO)

    fun amountAsPercentage(): BigDecimal {
        return amount * (100).toBigDecimal()
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

    override fun toString(): String {
        return when (this.name) {
            "COMMON" -> "Common (${this.value})"
            "UNCOMMON" -> "Uncommon (${this.value})"
            "RARE" -> "Rare (${this.value})"
            "MYTHIC" -> "Mythic (${this.value})"
            else -> "${this.name} (${this.value})"
        }
    }

    companion object {
        fun fromInt(int: Int): GameRarity {
            return entries.first { it.value == int }
        }
    }
}